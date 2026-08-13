package aethereal.lib.json;

public class JSONObject {
    private final org.json.JSONObject delegate;

    public JSONObject() {
        this.delegate = new org.json.JSONObject();
    }

    public JSONObject(String json) {
        this.delegate = new org.json.JSONObject(json);
    }

    public JSONObject(org.json.JSONObject delegate) {
        this.delegate = delegate;
    }

    public org.json.JSONObject unwrap() {
        return delegate;
    }

    public JSONObject c(String key, Object value) {
        if (value instanceof JSONObject jsonObject) {
            delegate.put(key, jsonObject.delegate);
        } else if (value instanceof JSONArray jsonArray) {
            delegate.put(key, jsonArray.unwrap());
        } else {
            delegate.put(key, value);
        }
        return this;
    }

    public JSONObject b(String key, boolean value) {
        delegate.put(key, value);
        return this;
    }

    public JSONObject b(String key, double value) {
        delegate.put(key, value);
        return this;
    }

    public JSONObject a(String key, Object value) {
        if (value instanceof JSONObject jsonObject) {
            delegate.put(key, jsonObject.delegate);
        } else if (value instanceof JSONArray jsonArray) {
            delegate.put(key, jsonArray.unwrap());
        } else {
            delegate.put(key, value);
        }
        return this;
    }

    public String toPrettyString(int indentFactor) {
        return delegate.toString(indentFactor);
    }

    public String l(String key) {
        return delegate.optString(key, null);
    }

    public boolean m(String key) {
        return delegate.has(key);
    }

    public double e(String key) {
        return delegate.optDouble(key);
    }

    public float f(String key) {
        return (float) delegate.optDouble(key);
    }

    public boolean b(String key) {
        return delegate.optBoolean(key);
    }

    public int h(String key) {
        return delegate.optInt(key);
    }

    public JSONObject j(String key) {
        org.json.JSONObject obj = delegate.optJSONObject(key);
        return obj == null ? null : new JSONObject(obj);
    }

    public JSONArray i(String key) {
        org.json.JSONArray arr = delegate.optJSONArray(key);
        return arr == null ? null : new JSONArray(arr);
    }

    public JSONArray y(String key) {
        return i(key);
    }

    public Object a(String key) {
        return delegate.opt(key);
    }

    public boolean a(String key, boolean defaultValue) {
        return delegate.optBoolean(key, defaultValue);
    }

    public int a(String key, int defaultValue) {
        return delegate.optInt(key, defaultValue);
    }

    public String a(String key, String defaultValue) {
        return delegate.optString(key, defaultValue);
    }

    public boolean q(String key) {
        return delegate.optBoolean(key);
    }

    public String a(int indentFactor) {
        return delegate.toString(indentFactor);
    }
}
