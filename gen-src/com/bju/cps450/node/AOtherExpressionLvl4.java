/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.bju.cps450.node;

import com.bju.cps450.analysis.*;

@SuppressWarnings("nls")
public final class AOtherExpressionLvl4 extends PExpressionLvl4
{
    private PExpressionLvl3 _expressionLvl3_;

    public AOtherExpressionLvl4()
    {
        // Constructor
    }

    public AOtherExpressionLvl4(
        @SuppressWarnings("hiding") PExpressionLvl3 _expressionLvl3_)
    {
        // Constructor
        setExpressionLvl3(_expressionLvl3_);

    }

    @Override
    public Object clone()
    {
        return new AOtherExpressionLvl4(
            cloneNode(this._expressionLvl3_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAOtherExpressionLvl4(this);
    }

    public PExpressionLvl3 getExpressionLvl3()
    {
        return this._expressionLvl3_;
    }

    public void setExpressionLvl3(PExpressionLvl3 node)
    {
        if(this._expressionLvl3_ != null)
        {
            this._expressionLvl3_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._expressionLvl3_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._expressionLvl3_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._expressionLvl3_ == child)
        {
            this._expressionLvl3_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._expressionLvl3_ == oldChild)
        {
            setExpressionLvl3((PExpressionLvl3) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
