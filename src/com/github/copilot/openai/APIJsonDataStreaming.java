/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.doubles.DoubleList
 *  it.unimi.dsi.fastutil.ints.IntList
 */
package com.github.copilot.openai;

import com.github.copilot.util.String2DoubleMap;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.List;

class APIJsonDataStreaming {
	private final List<String> text = new ArrayList<String>();
	private final StringBuilder joinedText = new StringBuilder();
	List<List<String>> tokens = new ArrayList<List<String>>();
	List<IntList> textOffset = new ArrayList<IntList>();
	List<DoubleList> logprobs = new ArrayList<DoubleList>();
	List<List<String2DoubleMap>> topLogprobs = new ArrayList<List<String2DoubleMap>>();

	APIJsonDataStreaming() {
	}

	void appendText(String text) {
		this.text.add(text);
		this.joinedText.append(text);
	}

	List<String> getText() {
		return this.text;
	}

	String getJoinedText() {
		return this.joinedText.toString();
	}
}
