/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pojos;

import java.io.Serializable;

/**
 *
 * @author Alex
 */
public class RangeIp implements Serializable{
    private String RangeBegin;
    private String RangeEnd;

    public String getRangeBegin() {
        return RangeBegin;
    }

    public void setRangeBegin(String RangeBegin) {
        this.RangeBegin = RangeBegin;
    }

    public String getRangeEnd() {
        return RangeEnd;
    }

    public void setRangeEnd(String RangeEnd) {
        this.RangeEnd = RangeEnd;
    }
    
}
