/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.bju.cps450.node;

import java.util.*;
import com.bju.cps450.analysis.*;

@SuppressWarnings("nls")
public final class AAssignStatementStatement extends PStatement
{
    private TIdentifier _identifier_;
    private final LinkedList<PExpression> _array_ = new LinkedList<PExpression>();
    private PExpression _expression_;

    public AAssignStatementStatement()
    {
        // Constructor
    }

    public AAssignStatementStatement(
        @SuppressWarnings("hiding") TIdentifier _identifier_,
        @SuppressWarnings("hiding") List<?> _array_,
        @SuppressWarnings("hiding") PExpression _expression_)
    {
        // Constructor
        setIdentifier(_identifier_);

        setArray(_array_);

        setExpression(_expression_);

    }

    @Override
    public Object clone()
    {
        return new AAssignStatementStatement(
            cloneNode(this._identifier_),
            cloneList(this._array_),
            cloneNode(this._expression_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAssignStatementStatement(this);
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

    public LinkedList<PExpression> getArray()
    {
        return this._array_;
    }

    public void setArray(List<?> list)
    {
        for(PExpression e : this._array_)
        {
            e.parent(null);
        }
        this._array_.clear();

        for(Object obj_e : list)
        {
            PExpression e = (PExpression) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._array_.add(e);
        }
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

    @Override
    public String toString()
    {
        return ""
            + toString(this._identifier_)
            + toString(this._array_)
            + toString(this._expression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._identifier_ == child)
        {
            this._identifier_ = null;
            return;
        }

        if(this._array_.remove(child))
        {
            return;
        }

        if(this._expression_ == child)
        {
            this._expression_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

        for(ListIterator<PExpression> i = this._array_.listIterator(); i.hasNext();)
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

        if(this._expression_ == oldChild)
        {
            setExpression((PExpression) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
