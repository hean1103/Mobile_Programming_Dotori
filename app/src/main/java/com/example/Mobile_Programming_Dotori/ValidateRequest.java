package com.example.Mobile_Programming_Dotori;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//아이디 중복확인 함수 id값만 넘겨받음
public class ValidateRequest extends StringRequest {

    public static final String URL = "http://13.124.77.84/UserValidate.php";
    private Map<String, String> parameters;

    public ValidateRequest(String id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("id", id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}