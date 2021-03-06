/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.bju.cps450.node;

import java.util.*;
import com.bju.cps450.analysis.*;

@SuppressWarnings("nls")
public final class ACallStatementStatement extends PStatement
{
    private PExpression _dot_;
    private TIdentifier _identifier_;
    private final LinkedList<PExpression> _expression_ = new LinkedList<PExpression>();

    public ACallStatementStatement()
    {
        // Constructor
    }

    public ACallStatementStatement(
        @SuppressWarnings("hiding") PExpression _dot_,
        @SuppressWarnings("hiding") TIdentifier _identifier_,
        @SuppressWarnings("hiding") List<?> _expression_)
    {
        // Constructor
        setDot(_dot_);

        setIdentifier(_identifier_);

        setExpression(_expression_);

    }

    @Override
    public Object clone()
    {
        return new ACallStatementStatement(
            cloneNode(this._dot_),
            cloneNode(this._identifier_),
            cloneList(this._expression_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACallStatementStatement(this);
    }

    public PExpression getDot()
    {
        return this._dot_;
    }

    public void setDot(PExpression node)
    {
        if(this._dot_ != null)
        {
            this._dot_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._dot_ = node;
    }

    public TIdentifier getIdentifier()
    {
        return this._identifier_;
    }

    public void setIdentifier(TIdentifier node)
    {
        if(this._identifier_ != null)
        {
            this._identifier_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._identifier_ = node;
    }

    public LinkedList<PExpression> getExpression()
    {
        return this._expression_;
    }

    public void setExpression(List<?> list)
    {
        for(PExpression e : this._expression_)
        {
            e.parent(null);
        }
        this._expression_.clear();

        for(Object obj_e : list)
        {
            PExpression e = (PExpression) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._expression_.add(e);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._dot_)
            + toString(this._identifier_)
            + toString(this._expression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._dot_ == child)
        {
            this._dot_ = null;
            return;
        }

        if(this._identifier_ == child)
        {
            this._identifier_ = null;
            return;
        }

        if(this._expression_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._dot_ == oldChild)
        {
            setDot((PExpression) newChild);
            return;
        }

        if(this._identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

        for(ListIterator<PExpression> i = this._expression_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PExpression) newChild);
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
