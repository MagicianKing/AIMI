/**
 *
 */
package com.yeying.aimi.protoco;


import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;

import java.io.Serializable;


/**
 * @author sparrow
 */
public abstract class BasicProtocol implements IProtocol,Serializable{

    public BasicProtocol() {
        super();
    }

    @Override
    public String uri() {
        return "";
    }

    @Override
    public abstract Arg[] serialize();

    @Override
    public abstract void unSerialize(Result result);

}
