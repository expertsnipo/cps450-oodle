/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.bju.cps450.node;

import com.bju.cps450.analysis.*;

@SuppressWarnings("nls")
public final class AAndExpressionLvl7 extends PExpressionLvl7
{
    private PExpressionLvl7 _expressionLvl7_;
    private TAnd _and_;
    private PExpressionLvl6 _expressionLvl6_;

    public AAndExpressionLvl7()
    {
        // Constructor
    }

    public AAndExpressionLvl7(
        @SuppressWarnings("hiding") PExpressionLvl7 _expressionLvl7_,
        @SuppressWarnings("hiding") TAnd _and_,
        @SuppressWarnings("hiding") PExpressionLvl6 _expressionLvl6_)
    {
        // Constructor
        setExpressionLvl7(_expressionLvl7_);

        setAnd(_and_);

        setExpressionLvl6(_expressionLvl6_);

    }

    @Override
    public Object clone()
    {
        return new AAndExpressionLvl7(
            cloneNode(this._expressionLvl7_),
            cloneNode(this._and_),
            cloneNode(this._expressionLvl6_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAndExpressionLvl7(this);
    }

    public PExpressionLvl7 getExpressionLvl7()
    {
        return this._expressionLvl7_;
    }

    public void setExpressionLvl7(PExpressionLvl7 node)
    {
        if(this._expressionLvl7_ != null)
        {
            this._expressionLvl7_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._expressionLvl7_ = node;
    }

    public TAnd getAnd()
    {
        return this._and_;
    }

    public void setAnd(TAnd node)
    {
        if(this._and_ != null)
        {
            this._and_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._and_ = node;
    }

    public PExpressionLvl6 getExpressionLvl6()
    {
        return this._expressionLvl6_;
    }

    public void setExpressionLvl6(PExpressionLvl6 node)
    {
        if(this._expressionLvl6_ != null)
        {
            this._expressionLvl6_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._expressionLvl6_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._expressionLvl7_)
            + toString(this._and_)
            + toString(this._expressionLvl6_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._expressionLvl7_ == child)
        {
            this._expressionLvl7_ = null;
            return;
        }

        if(this._and_ == child)
        {
            this._and_ = null;
            return;
        }

        if(this._expressionLvl6_ == child)
        {
            this._expressionLvl6_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._expressionLvl7_ == oldChild)
        {
            setExpressionLvl7((PExpressionLvl7) newChild);
            return;
        }

        if(this._and_ == oldChild)
        {
            setAnd((TAnd) newChild);
            return;
        }

        if(this._expressionLvl6_ == oldChild)
        {
            setExpressionLvl6((PExpressionLvl6) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
