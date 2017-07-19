/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author VBrum
 */
public class Helper {

    public static List<String> keysToSortedList(Collection<String> collection) {
        List<String> list = new ArrayList<String>(collection);

        java.util.Collections.sort(list);

        return list;
    }
}
