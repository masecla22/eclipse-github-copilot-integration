/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
 *  it.unimi.dsi.fastutil.objects.ObjectSortedSet
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.github.copilot.editor;

import com.github.copilot.completions.CopilotInlayList;
import com.github.copilot.request.EditorRequest;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class EditorRequestResultList {
    private final EditorRequest request;
    private final Object inlayLock;
    private final ObjectLinkedOpenHashSet<CopilotInlayList> inlayLists;
    private int index;
    private int maxShownIndex;
    private boolean hasOnDemandCompletions;

    public EditorRequestResultList(EditorRequest request) {
        if (request == null) {
            EditorRequestResultList.$$$reportNull$$$0(0);
        }
        this.inlayLock = new Object();
        this.inlayLists = new ObjectLinkedOpenHashSet();
        this.index = 0;
        this.maxShownIndex = -1;
        this.request = request;
    }

    public EditorRequest getRequest() {
        return this.request;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void resetInlays() {
        Object object = this.inlayLock;
        synchronized (object) {
            this.inlayLists.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addInlays(CopilotInlayList inlays) {
        if (inlays == null) {
            EditorRequestResultList.$$$reportNull$$$0(1);
        }
        Object object = this.inlayLock;
        synchronized (object) {
            this.inlayLists.add((Object)inlays);
            this.maxShownIndex = Math.max(0, this.maxShownIndex);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
        public CopilotInlayList getCurrentCompletion() {
        Object object = this.inlayLock;
        synchronized (object) {
            return EditorRequestResultList.getAtIndexLocked(this.inlayLists, this.index);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
        public List<CopilotInlayList> getAllShownCompletion() {
        Object object = this.inlayLock;
        synchronized (object) {
            return this.inlayLists.stream().limit(this.maxShownIndex + 1).collect(Collectors.toList());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean hasCurrent() {
        Object object = this.inlayLock;
        synchronized (object) {
            return this.index >= 0 && this.index < this.inlayLists.size();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean hasPrev() {
        Object object = this.inlayLock;
        synchronized (object) {
            return this.index >= 1;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean hasNext() {
        Object object = this.inlayLock;
        synchronized (object) {
            return this.index + 1 < this.inlayLists.size();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
        public CopilotInlayList getPrevCompletion() {
        Object object = this.inlayLock;
        synchronized (object) {
            if (this.index <= 0) {
                this.index = 0;
                return null;
            }
            --this.index;
            return EditorRequestResultList.getAtIndexLocked(this.inlayLists, this.index);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
        public CopilotInlayList getNextCompletion() {
        Object object = this.inlayLock;
        synchronized (object) {
            if (this.index + 1 >= this.inlayLists.size()) {
                this.index = this.inlayLists.size() - 1;
                return null;
            }
            ++this.index;
            this.maxShownIndex = Math.max(this.maxShownIndex, this.index);
            return EditorRequestResultList.getAtIndexLocked(this.inlayLists, this.index);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean hasOnDemandCompletions() {
        Object object = this.inlayLock;
        synchronized (object) {
            return this.hasOnDemandCompletions || this.inlayLists.size() > 1;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setHasOnDemandCompletions() {
        Object object = this.inlayLock;
        synchronized (object) {
            this.hasOnDemandCompletions = true;
        }
    }

        private static CopilotInlayList getAtIndexLocked(ObjectSortedSet<CopilotInlayList> inlays, int index) {
        if (inlays == null) {
            EditorRequestResultList.$$$reportNull$$$0(2);
        }
        return inlays.stream().skip(index).findFirst().orElse(null);
    }

    public Object getInlayLock() {
        return this.inlayLock;
    }

    public ObjectLinkedOpenHashSet<CopilotInlayList> getInlayLists() {
        return this.inlayLists;
    }

    public int getIndex() {
        return this.index;
    }

    public int getMaxShownIndex() {
        return this.maxShownIndex;
    }

    public boolean isHasOnDemandCompletions() {
        return this.hasOnDemandCompletions;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setMaxShownIndex(int maxShownIndex) {
        this.maxShownIndex = maxShownIndex;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EditorRequestResultList)) {
            return false;
        }
        EditorRequestResultList other = (EditorRequestResultList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getIndex() != other.getIndex()) {
            return false;
        }
        if (this.getMaxShownIndex() != other.getMaxShownIndex()) {
            return false;
        }
        if (this.isHasOnDemandCompletions() != other.isHasOnDemandCompletions()) {
            return false;
        }
        EditorRequest this$request = this.getRequest();
        EditorRequest other$request = other.getRequest();
        if (this$request == null ? other$request != null : !this$request.equals(other$request)) {
            return false;
        }
        Object this$inlayLock = this.getInlayLock();
        Object other$inlayLock = other.getInlayLock();
        if (this$inlayLock == null ? other$inlayLock != null : !this$inlayLock.equals(other$inlayLock)) {
            return false;
        }
        ObjectLinkedOpenHashSet<CopilotInlayList> this$inlayLists = this.getInlayLists();
        ObjectLinkedOpenHashSet<CopilotInlayList> other$inlayLists = other.getInlayLists();
        return !(this$inlayLists == null ? other$inlayLists != null : !this$inlayLists.equals(other$inlayLists));
    }

    protected boolean canEqual(Object other) {
        return other instanceof EditorRequestResultList;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getIndex();
        result = result * 59 + this.getMaxShownIndex();
        result = result * 59 + (this.isHasOnDemandCompletions() ? 79 : 97);
        EditorRequest $request = this.getRequest();
        result = result * 59 + ($request == null ? 43 : $request.hashCode());
        Object $inlayLock = this.getInlayLock();
        result = result * 59 + ($inlayLock == null ? 43 : $inlayLock.hashCode());
        ObjectLinkedOpenHashSet<CopilotInlayList> $inlayLists = this.getInlayLists();
        result = result * 59 + ($inlayLists == null ? 43 : $inlayLists.hashCode());
        return result;
    }

    public String toString() {
        return "EditorRequestResultList(request=" + this.getRequest() + ", inlayLists=" + this.getInlayLists() + ", index=" + this.getIndex() + ", maxShownIndex=" + this.getMaxShownIndex() + ", hasOnDemandCompletions=" + this.isHasOnDemandCompletions() + ")";
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        Object[] objectArray;
        Object[] objectArray2;
        Object[] objectArray3 = new Object[3];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "request";
                break;
            }
            case 1: 
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "inlays";
                break;
            }
        }
        objectArray2[1] = "com/github/copilot/editor/EditorRequestResultList";
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[2] = "<init>";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[2] = "addInlays";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[2] = "getAtIndexLocked";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for parameter '%s' of %s.%s must not be null", objectArray));
    }
}

