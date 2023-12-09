package com.wup.write2database.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 科目一试题表
 * @TableName test_paper
 */
@TableName(value ="test_paper")
@Data
public class TestPaper implements Serializable {
    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 题目代码编号
     */
    private String code;

    /**
     * 类型 1 判断题 2 选择题
     */
    private Integer type;

    /**
     * 问题
     */
    private String question;

    /**
     * 选择项 对于判断题为空
     */
    private String selectItem;

    /**
     * 答案
     */
    private String answer;

    /**
     * 题目解析
     */
    private String analysis;

    /**
     * 题目图片的url
     */
    private String picUrl;

    /**
     * 题目关键字
     */
    private String keyWords;

    /**
     * 题目星级
     */
    private Integer star;

    /**
     * 错误率
     */
    private Double errRate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TestPaper other = (TestPaper) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getQuestion() == null ? other.getQuestion() == null : this.getQuestion().equals(other.getQuestion()))
            && (this.getSelectItem() == null ? other.getSelectItem() == null : this.getSelectItem().equals(other.getSelectItem()))
            && (this.getAnswer() == null ? other.getAnswer() == null : this.getAnswer().equals(other.getAnswer()))
            && (this.getAnalysis() == null ? other.getAnalysis() == null : this.getAnalysis().equals(other.getAnalysis()))
            && (this.getPicUrl() == null ? other.getPicUrl() == null : this.getPicUrl().equals(other.getPicUrl()))
            && (this.getKeyWords() == null ? other.getKeyWords() == null : this.getKeyWords().equals(other.getKeyWords()))
            && (this.getStar() == null ? other.getStar() == null : this.getStar().equals(other.getStar()))
            && (this.getErrRate() == null ? other.getErrRate() == null : this.getErrRate().equals(other.getErrRate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getQuestion() == null) ? 0 : getQuestion().hashCode());
        result = prime * result + ((getSelectItem() == null) ? 0 : getSelectItem().hashCode());
        result = prime * result + ((getAnswer() == null) ? 0 : getAnswer().hashCode());
        result = prime * result + ((getAnalysis() == null) ? 0 : getAnalysis().hashCode());
        result = prime * result + ((getPicUrl() == null) ? 0 : getPicUrl().hashCode());
        result = prime * result + ((getKeyWords() == null) ? 0 : getKeyWords().hashCode());
        result = prime * result + ((getStar() == null) ? 0 : getStar().hashCode());
        result = prime * result + ((getErrRate() == null) ? 0 : getErrRate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", code=").append(code);
        sb.append(", type=").append(type);
        sb.append(", question=").append(question);
        sb.append(", selectItem=").append(selectItem);
        sb.append(", answer=").append(answer);
        sb.append(", analysis=").append(analysis);
        sb.append(", picUrl=").append(picUrl);
        sb.append(", keyWords=").append(keyWords);
        sb.append(", star=").append(star);
        sb.append(", errRate=").append(errRate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}