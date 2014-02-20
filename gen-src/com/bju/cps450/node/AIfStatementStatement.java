/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.bju.cps450.node;

import java.util.*;
import com.bju.cps450.analysis.*;

@SuppressWarnings("nls")
public final class AIfStatementStatement extends PStatement
{
    private PExpression _expression_;
    private final LinkedList<PStatement> _if_ = new LinkedList<PStatement>();
    private final LinkedList<PStatement> _else_ = new LinkedList<PStatement>();

    public AIfStatementStatement()
    {
        // Constructor
    }

    public AIfStatementStatement(
        @SuppressWarnings("hiding") PExpression _expression_,
        @SuppressWarnings("hiding") List<?> _if_,
        @SuppressWarnings("hiding") List<?> _else_)
    {
        // Constructor
        setExpression(_expression_);

        setIf(_if_);

        setElse(_else_);

    }

    @Override
    public Object clone()
    {
        return new AIfStatementStatement(
            cloneNode(this._expression_),
            cloneList(this._if_),
            cloneList(this._else_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIfStatementStatement(this);
    }

    public PExpression getExpression()
    {
        return this._expression_;
    }

    public void setExpression(PExpression node)
    {
        if(this._expression_ != null)
        {
            this._expression_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._expression_ = node;
    }

    public LinkedList<PStatement> getIf()
    {
        return this._if_;
    }

    public void setIf(List<?> list)
    {
        for(PStatement e : this._if_)
        {
            e.parent(null);
        }
        this._if_.clear();

        for(Object obj_e : list)
        {
            PStatement e = (PStatement) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._if_.add(e);
        }
    }

    public LinkedList<PStatement> getElse()
    {
        return this._else_;
    }

    public void setElse(List<?> list)
    {
        for(PStatement e : this._else_)
        {
            e.parent(null);
        }
        this._else_.clear();

        for(Object obj_e : list)
        {
            PStatement e = (PStatement) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._else_.add(e);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._expression_)
            + toString(this._if_)
            + toString(this._else_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._expression_ == child)
        {
            this._expression_ = null;
            return;
        }

        if(this._if_.remove(child))
        {
            return;
        }

        if(this._else_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._expression_ == oldChild)
        {
            setExpression((PExpression) newChild);
            return;
        }

        for(ListIterator<PStatement> i = this._if_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PStatement) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        for(ListIterator<PStatement> i = this._else_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PStatement) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        throw new RuntimeException("Not a child.");
    }
}