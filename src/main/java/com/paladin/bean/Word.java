package com.paladin.bean;
/**
 * Copyright (C) 2011 Erhu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 单词
 *
 * @author: Erhu
 * @date: 11-5-9 下午4:10
 */
public class Word {
    private int id;
    /**
     * 拼写
     */
    private String spelling;
    /**
     * 音标
     */
    private String phonetic_symbol;
    /**
     * 含义
     */
    private String meaning;
    /**
     * 被查询次数
     */
    private int hit;
    /**
     * 已经记住
     */
    private boolean remembed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpelling() {
        return spelling;
    }

    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }

    public String getPhonetic_symbol() {
        return phonetic_symbol;
    }

    public void setPhonetic_symbol(String phonetic_symbol) {
        this.phonetic_symbol = phonetic_symbol;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public boolean isRemembed() {
        return remembed;
    }

    public void setRemembed(boolean remembed) {
        this.remembed = remembed;
    }
}
