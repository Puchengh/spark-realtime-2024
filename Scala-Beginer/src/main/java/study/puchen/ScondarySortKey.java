package study.puchen;

import scala.math.Ordered;

import java.io.Serializable;

/**
 * @program: scalatest
 * @description:
 * @author: Puchen
 * @create: 2019-07-18 15:44
 */
public class ScondarySortKey implements Ordered<ScondarySortKey>, Serializable {

    public ScondarySortKey(int first, int second) {
        this.first = first;
        this.second = second;
    }

    private int first;
    private int second;

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int compare(ScondarySortKey that) {
        //返回0  代表相等
        if(this.first != that.first){
            return this.first-that.first;
        }else{
            return this.second-that.second;
        }
    }

    public boolean $less(ScondarySortKey that) {
        if(this.first < that.first){
            return true;
        }else if(this.first == that.first && this.second < that.second) {
            return true;
        }
        return false;
    }

    public boolean $greater(ScondarySortKey that) {
        if(this.first > that.first){
            return true;
        }else if(this.first == that.first && this.second > that.second) {
            return true;
        }
        return false;
    }

    public boolean $less$eq(ScondarySortKey that) {
        if(this.$less(that)){
            return true;
        }else if(this.first == that.first && this.second == that.second){
            return true;
        }
        return false;
    }

    public boolean $greater$eq(ScondarySortKey that) {
        if(this.$greater(that)){
            return true;
        }else if(this.first == that.first && this.second == that.second){
            return true;
        }
        return false;
    }

    public int compareTo(ScondarySortKey that) {
        return this.compare(that);
    }
}
