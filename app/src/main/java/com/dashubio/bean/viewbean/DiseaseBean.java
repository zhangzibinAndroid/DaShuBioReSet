package com.dashubio.bean.viewbean;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/28 0028
 * 时间： 下午 2:23
 * 描述： 疾病，父亲，母亲，子女实体类封装
 */

public class DiseaseBean {

    /**
     * name : 糖尿病
     * id : 3
     */

    private String name;
    private String id;

    public DiseaseBean(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
