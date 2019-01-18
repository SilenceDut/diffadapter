package com.silencedut.core

/**
 * 定义简单的数据对象，只作数据存储，不增加逻辑
 *
 */

data class DataObject1<F1>(var field1: F1)

data class DataObject2<F1, F2>(var field1: F1, var field2: F2)

data class DataObject3<F1, F2, F3>(var field1: F1, var field2: F2, var field3: F3)

data class DataObject4<F1, F2, F3, F4>(var field1: F1, var field2: F2, var field3: F3, var field4: F4)

data class DataObject5<F1, F2, F3, F4, F5>(var field1: F1, var field2: F2, var field3: F3, var field4: F4, var field5: F5)

data class DataObject6<F1, F2, F3, F4, F5, F6>(var field1: F1, var field2: F2, var field3: F3, var field4: F4, var field5: F5, var field6: F6)

data class DataObject7<F1, F2, F3, F4, F5, F6, F7>(var field1: F1, var field2: F2, var field3: F3, var field4: F4, var field5: F5, var field6: F6, var field7: F7)

data class DataObject8<F1, F2, F3, F4, F5, F6, F7, F8>(var field1: F1, var field2: F2, var field3: F3, var field4: F4, var field5: F5, var field6: F6, var field7: F7, var field8: F8)

data class DataObject9<F1, F2, F3, F4, F5, F6, F7, F8, F9>(var field1: F1, var field2: F2, var field3: F3, var field4: F4, var field5: F5, var field6: F6, var field7: F7, var field8: F8, var field9: F9)
