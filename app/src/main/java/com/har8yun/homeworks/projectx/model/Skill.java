package com.har8yun.homeworks.projectx.model;


public class Skill {
    private String mSkillName;
    private Integer mSkillCount;

    public Skill()
    {}

    public Skill(String skillName, int skillCount) {
        mSkillName = skillName;
        mSkillCount = skillCount;
    }

    public String getSkillName() {
        return mSkillName;
    }

    public void setSkillName(String skillName) {
        mSkillName = skillName;
    }

    public Integer getSkillCount() {
        return mSkillCount;
    }

    public void setSkillCount(Integer skillCount) {
        mSkillCount = skillCount;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "mSkillName='" + mSkillName + '\'' +
                ", mSkillCount=" + mSkillCount +
                '}';
    }
}
