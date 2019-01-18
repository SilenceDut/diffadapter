package com.silencedut.diffadapter.utils;

import java.util.List;

/**
 * @author SilenceDut
 * @date 2019/1/19
 */
public interface ListChangedCallback<T> {
    void onListChanged(List<T> currentList);
}
