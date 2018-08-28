package com.pirate.esredisdemo.enums;

import org.springframework.util.Assert;

public enum EsEnum {

    ACCOUNT("bank", "account"),

    MAX_SIZE(1000);

    String index;
    String type;
    int size;

    EsEnum(String index, String type) {
        this.index = index;
        this.type = type;
    }

    EsEnum(int size) {
        this.size = size;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public static int getPage(int num, int skip) {
        Assert.isTrue(num > 0, "num 不能小于0");
        Assert.isTrue(skip > 0, "num 不能小于0");

        int i = num % skip;
        if (i == 0) {
            return num / skip;
        } else {
            return num / skip + 1;
        }
    }
}
