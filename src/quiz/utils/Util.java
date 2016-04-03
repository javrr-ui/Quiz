/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quiz.utils;

/**
 *
 * @author Administrador
 */
public class Util {
    //workaround for mutiple varargs
    public static <T> T[] varargs (T...myVararg) {
        return myVararg;
    }
}
