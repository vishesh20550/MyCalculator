package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private var textView : TextView? = null
    private var isOperatorActive : Boolean = false
    private var isDecimalActive : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
    }
    fun onDigitClick(view: View){
        var str =(view as Button).text
        if(str.equals("0") && (textView?.text==null || textView?.text!!.isEmpty() || textView?.text?.get(textView?.text!!.length-1)?.equals('0') == true)){
            return
        }
        if(str.equals(".") ){
            if(textView?.text==null || textView?.text!!.isEmpty() || isOperatorActive){
                str = "0.";
            } else if(isDecimalActive){
                return;
            }
            isDecimalActive = true;
        }
        textView?.append(str)
        if(isOperatorActive){
            isOperatorActive = false;
        }
    }
    fun onClearClick(view:View){
        textView?.text = ""
        isOperatorActive=false;
        isDecimalActive=false;
    }
    fun onOperatorClicked(view : View){

        if(!isOperatorActive){
            val operator = (view as Button).text
            if(textView?.text==null && textView?.text!!.isEmpty()){
                if(operator== "-"){
                    textView?.append("(-)");
                }
                return;
            }
            if(textView?.text.toString()[textView?.text!!.length-1] == '.'){
                Toast.makeText(this, "'.' should not be in the end",Toast.LENGTH_SHORT).show();
                return
            }
            isOperatorActive = true
            if(operator.equals("=") ){
                if(textView?.text==null || textView?.text!!.isEmpty()){
                    Toast.makeText(this, "No input provided :^",Toast.LENGTH_SHORT).show();
                }else{
                    printOutput()
                }
            }else{
                textView?.append(operator);
            }
            isDecimalActive = false
        }else{
            if((view as Button).text.equals("-")){
                if (textView?.text?.get(textView?.text!!.length - 1)?.equals('+') == true) {
                    onDelClicked(view)
                    textView?.append("-");
                    isOperatorActive=true;
                }else if(textView?.text?.get(textView?.text!!.length - 1)?.equals('-') == true){
                    onDelClicked(view)
                    textView?.append("+");
                    isOperatorActive=true;
                }else if(textView?.text?.get(textView?.text!!.length - 1)?.equals('*') == true || textView?.text?.get(textView?.text!!.length - 1)?.equals('/') == true){
                    textView?.append("(-)")
                }
            }else if((view as Button).text.equals("+")){
                if (textView?.text?.get(textView?.text!!.length - 1)?.equals('-') == true) {
                    onDelClicked(view)
                    textView?.append("-");
                    isOperatorActive=true;
                }
            }
        }
    }
    private fun printOutput(){
        val str = textView?.text;
        if(str?.contains('+') == false && !str.contains('-') && !str.contains('*') && !str.contains('/')){
            return;
        }else{

        }
    }
    fun onDelClicked(view : View){
        if(textView?.text!=null && textView?.text!!.isNotEmpty()){
            if (isOperatorActive)
                isOperatorActive=false;
            val str = StringBuilder(textView?.text.toString())
            if(str[str.length-1] == '.'){
                if(str[str.length-2] == '0'){
                    str.deleteCharAt(str.length-1)
                }
                isDecimalActive=false;
            }else if(str[str.length-1] == ')'){
                str.deleteCharAt(str.length - 1);
                str.deleteCharAt(str.length - 1);
            }
            str.deleteCharAt(str.length - 1)
            if(str.isNotEmpty()){
                if(str[str.length-1] == '+' || str[str.length-1] == '/' || str[str.length-1] == '-' || str[str.length-1] == '*' || str[str.length-1] == '='){ isOperatorActive = true; }
            }
            textView?.text = str.toString()
        }
    }
}