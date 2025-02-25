/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.beans.exampleProject.config;

import com.patryklikus.winter.beans.Beans;
import com.patryklikus.winter.beans.exampleProject.models.Color;
import com.patryklikus.winter.lifecycle.Close;
import com.patryklikus.winter.lifecycle.Init;
import com.patryklikus.winter.lifecycle.Run;

import java.util.Arrays;
import java.util.List;

@Beans
public class Config {
    public String red() {
        return "red";
    }

    @Init(enabled = false, order = 1)
    @Run(enabled = false)
    @Close(enabled = false, order = 3)
    public Color redColor(String red) {
        return new Color(red);
    }

    public List<String> textList() {
        return List.of("Hello", "World", "!");
    }

    public String text(List<String> textList) {
        return String.join(" ", textList);
    }

    public int[] numbers() {
        return new int[]{1, 2, 3};
    }

    public int sum(int[] numbers) {
        return Arrays.stream(numbers).sum();
    }
}