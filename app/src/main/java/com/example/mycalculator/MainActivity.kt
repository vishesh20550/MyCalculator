package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.ArithmeticException
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
        val operator = (view as Button).text
        if(!isOperatorActive){
            if(textView?.text==null || textView?.text == ""){
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
            if(operator.equals("-")){
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
            }else if(operator.equals("+")){
                if (textView?.text?.get(textView?.text!!.length - 1)?.equals('-') == true) {
                    onDelClicked(view)
                    textView?.append("-");
                    isOperatorActive=true;
                }
            }
        }
    }
    private fun printOutput(){
        val str = textView?.text.toString();
        if(!str.contains('+') && !str.contains('-') && !str.contains('*') && !str.contains('/')){
            return;
        }else{
            try {
                var isNegative = false;
                var start = 0;
                var num1 = ""
                var num2 = ""
                var operatorActive = '0'
                var i =0
                while(i < str.length){
                    if(str[i] == '('){
                        isNegative = true
                        i+=3;
                        start=i
                    }

                    if(str[i] == '+' || str[i] == '*' ||str[i] == '/' ||str[i] == '-' || i==str.length-1){
                       if(num1 == ""){
                           num1= str.substring(start,i);
                           if(isNegative){
                               num1= "-$num1";
                               isNegative=false;
                           }
                           num2=""
                           start = i+1;
                           operatorActive = str[i]
                       }else if(num2 == ""){
                           if(i==str.length-1){
                               i++;
                           }
                           num2 = str.substring(start,i)
                           if(isNegative){
                               num2= "-$num2";
                               isNegative=false;
                           }
                           start = i+1;
                           var num1Double = num1.toDouble();
                           val num2Double = num2.toDouble();
                           if(operatorActive == '+'){
                               num1Double += num2Double
                           }else if(operatorActive == '-'){
                               num1Double -= num2Double
                           }else if(operatorActive == '*'){
                               num1Double *= num2Double
                           }else if(operatorActive == '/'){
                               num1Double /= num2Double
                               if(num1Double== Double.POSITIVE_INFINITY || num1Double== Double.NEGATIVE_INFINITY){
                                   Toast.makeText(this, "Infinity >>>>>",Toast.LENGTH_SHORT).show();
                                   textView?.text =""
                                   isDecimalActive=true;
                                   isOperatorActive=false;
                                   return;
                               }
                           }
                           num1 = num1Double.toString()
                           num2=""
                           if(i<str.length-1){
                               operatorActive = str[i]
                           }
                           if(i>=str.length){
                               var strTemp = num1Double.toString();
                               if(num1Double<0){
                                   strTemp = "(-)${strTemp.substring(1)}"
                               }
                               textView?.text = strTemp;
                               isDecimalActive=true;
                               isOperatorActive=false;
                           }
                       }
                    }
                    i++;
                }
            }catch (e  : ArithmeticException){
                Toast.makeText(this, "Wrong Calculation parameters (eg. divide by zero)",Toast.LENGTH_SHORT).show();
                textView?.text =""
                isDecimalActive=true;
                isOperatorActive=false;
                e.printStackTrace();
                return
//                Log.i("Arithmetic Error",e.toString());
            }
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