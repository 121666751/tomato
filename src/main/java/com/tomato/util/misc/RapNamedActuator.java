package com.tomato.util.misc;

import java.io.Serializable;

/**
 * Rap通用的执行器
 *
 * @author CaiBo
 * @version $Id$
 * @since 2017年7月25日 上午10:24:05
 */
public interface RapNamedActuator extends Serializable {

    /**
     * 执行器名称，唯一标识
     *
     * @return
     */
    String getName();

    /**
     * 执行器顺序，越小优先级越高
     *
     * @return
     */
    int order();
}
