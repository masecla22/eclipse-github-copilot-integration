/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.intellij.execution.ExecutionException
 *  com.intellij.execution.configurations.GeneralCommandLine
 *  com.intellij.execution.process.KillableProcessHandler
 *  com.intellij.execution.process.ProcessAdapter
 *  com.intellij.execution.process.ProcessEvent
 *  com.intellij.execution.process.ProcessListener
 *  com.intellij.execution.process.ProcessOutputTypes
 *  com.intellij.openapi.Disposable
 *  com.intellij.openapi.diagnostic.Logger
 *  com.intellij.openapi.util.Key
 *  com.intellij.util.io.BaseDataReader$SleepingPolicy
 *  com.intellij.util.io.BaseOutputReader$Options
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.concurrency.AsyncPromise
 *  org.jetbrains.concurrency.Promise
 *  org.jetbrains.concurrency.Promises
 */
package com.github.copilot.lang.agent;

import com.github.copilot.lang.agent.CopilotAgentProcessService;
import com.github.copilot.lang.agent.CopilotAgentUtil;
import com.github.copilot.lang.agent.rpc.JsonRpcCommand;
import com.github.copilot.lang.agent.rpc.JsonRpcCommandSender;
import com.github.copilot.lang.agent.rpc.JsonRpcMessageParser;
import com.github.copilot.lang.agent.rpc.JsonRpcNotification;
import com.github.copilot.lang.agent.rpc.NullCommandSender;
import com.github.copilot.lang.agent.vscodeRpc.DefaultJsonRpcMessageHandler;
import com.github.copilot.lang.agent.vscodeRpc.VSCodeJsonRpcCommandSender;
import com.github.copilot.lang.agent.vscodeRpc.VSCodeJsonRpcParser;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessListener;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.util.io.BaseDataReader;
import com.intellij.util.io.BaseOutputReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.concurrency.AsyncPromise;
import org.jetbrains.concurrency.Promise;
import org.jetbrains.concurrency.Promises;

public class CopilotAgentProcessServiceImpl
implements CopilotAgentProcessService,
Disposable {
    private static final Logger LOG = Logger.getInstance(CopilotAgentProcessServiceImpl.class);
    private final AtomicInteger requestId = new AtomicInteger();
    private final Object lock = new Object();
    private final KillableProcessHandler agentProcess;
    private final Object outputLock = new Object();
    private final JsonRpcCommandSender messageSender;
    private final DefaultJsonRpcMessageHandler messageHandler = new DefaultJsonRpcMessageHandler();
    private final JsonRpcMessageParser jsonParser = new VSCodeJsonRpcParser(this.messageHandler);

    public CopilotAgentProcessServiceImpl() {
        KillableProcessHandler process = null;
        try {
            Path nodePath = CopilotAgentUtil.getNodeExecutablePath();
            Path agentDistPath = CopilotAgentUtil.getAgentDirectoryPath();
            if (nodePath != null && agentDistPath != null) {
                Path agentFilePath = agentDistPath.resolve("agent.js");
                if (Files.exists(agentFilePath, new LinkOption[0])) {
                    process = this.launchAgent(nodePath.toString(), agentFilePath.toString());
                } else {
                    LOG.error("bundled agent.js file not found: " + agentFilePath);
                }
            }
        }
        catch (Exception e) {
            LOG.error("Failed to launch agent process", (Throwable)e);
        }
        this.agentProcess = process;
        this.messageSender = process == null ? new NullCommandSender() : new VSCodeJsonRpcCommandSender(this.agentProcess.getProcessInput());
    }

    @Override
    public boolean isSupported() {
        return this.agentProcess != null;
    }

    public void dispose() {
        try {
            if (this.agentProcess != null && !this.agentProcess.isProcessTerminated()) {
                this.agentProcess.killProcess();
            }
        }
        catch (Exception e) {
            LOG.error("error terminating agent", (Throwable)e);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
        public <T> Promise<T> executeCommand(JsonRpcCommand<T> command) {
        AsyncPromise<T> asyncPromise;
        if (command == null) {
            throw new IllegalStateException("command cannot be null!");
        }
        if (this.agentProcess == null) {
            Promise promise = Promises.rejectedPromise((String)"agent process unavailable");
            if (promise == null) {
                throw new IllegalStateException("promise cannot be null!");
            }
            return promise;
        }
        Object object = this.lock;
        synchronized (object) {
            int nextRequestId = this.requestId.getAndIncrement();
            try {
                this.messageSender.sendCommand(nextRequestId, command);
                LOG.debug("Registering result for request ID: " + nextRequestId);
                asyncPromise = this.messageHandler.addPendingRequest(nextRequestId, command.getCommandName(), command.getResponseType());
            }
            catch (Exception e) {
                LOG.error("exception sending command to Copilot agent, request id: " + nextRequestId, (Throwable)e);
                Promise promise = Promises.rejectedPromise((String)e.getMessage());
                // MONITOREXIT @DISABLED, blocks:[3, 4] lbl20 : MonitorExitStatement: MONITOREXIT : var2_2
                if (promise == null) {
                    throw new IllegalStateException("promise cannot be null!");
                }
                return promise;
            }
        }
        if (asyncPromise == null) {
            throw new IllegalStateException("asyncPromise cannot be null!");
        }
        return asyncPromise;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void executeNotification(JsonRpcNotification notification) {
        if (notification == null) {
            throw new IllegalStateException("notification cannot be null!");
        }
        if (this.agentProcess == null) {
            return;
        }
        Object object = this.lock;
        synchronized (object) {
            try {
                LOG.debug("Sending notification, name: " + notification.getCommandName());
                this.messageSender.sendNotification(notification);
            }
            catch (Exception e) {
                LOG.error("exception sending notification to Copilot agent", (Throwable)e);
            }
        }
    }

    private KillableProcessHandler launchAgent(String nodePath, String agentFilePath) throws ExecutionException {
        if (nodePath == null) {
            throw new IllegalStateException("nodePath cannot be null!");
        }
        if (agentFilePath == null) {
            throw new IllegalStateException("agentFilePath cannot be null!");
        }
        GeneralCommandLine cmdline = new GeneralCommandLine(new String[]{nodePath, agentFilePath, "jsonrpc"});
        KillableProcessHandler process = new KillableProcessHandler(cmdline){
            {
                this.addProcessListener((ProcessListener)new ProcessAdapter(){

                    /*
                     * WARNING - Removed try catching itself - possible behaviour change.
                     */
                    public void onTextAvailable(ProcessEvent event, Key outputType) {
                        if (event == null) {
                        	throw new IllegalStateException("event cannot be null");
                        }
                        if (outputType == null) {
                        	throw new IllegalStateException("outputType cannot be null");
                        }
                        if (LOG.isTraceEnabled()) {
                            LOG.trace(String.format("agent onTextAvailable: %s, outputType: %s", event.getText(), outputType));
                        }
                        if (outputType.equals((Object)ProcessOutputTypes.STDOUT)) {
                            Object object = CopilotAgentProcessServiceImpl.this.outputLock;
                            synchronized (object) {
                                CopilotAgentProcessServiceImpl.this.jsonParser.append(event.getText());
                            }
                        }
                    }

                    public void processTerminated(ProcessEvent event) {
                        if (event == null) {
                        	throw new IllegalStateException("event cannot be null");
                        }
                        CopilotAgentProcessServiceImpl.this.jsonParser.close();
                    }

                    
                });
            }

            protected // Could not load outer class - annotation placement on inner may be incorrect
             BaseOutputReader.Options readerOptions() {
                return new BaseOutputReader.Options(){

                    public BaseDataReader.SleepingPolicy policy() {
                        return BaseDataReader.SleepingPolicy.BLOCKING;
                    }

                    public boolean splitToLines() {
                        return false;
                    }
                };
            }
        };
        process.startNotify();
        return process;
    }

    
}

