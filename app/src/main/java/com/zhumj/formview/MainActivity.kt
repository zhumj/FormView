package com.zhumj.formview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val datalist = ArrayList<String>()
//        datalist.add("变速箱")
//        datalist.add("8档\u3000手自一体")
//        datalist.add("综合工况油耗(L/100km)")
//        datalist.add("9.28")
//        datalist.add("排量")
//        datalist.add("2.0")
//        datalist.add("官方0-100公里加速时间(s)")
//        datalist.add("7.10")
//        datalist.add("最高车速(km/h)")
//        datalist.add("225")
//        datalist.add("乘员人数(区间)(个)")
//        datalist.add("12")

        datalist.add("行1列1")
        datalist.add("行1列2")
        datalist.add("行2列1")
        datalist.add("行2列2")
        datalist.add("行3列1")
        datalist.add("行3列2")
        datalist.add("行4列1")
        datalist.add("行4列2")
        datalist.add("行5列1")

        formView.setDataList(datalist)
    }
}
