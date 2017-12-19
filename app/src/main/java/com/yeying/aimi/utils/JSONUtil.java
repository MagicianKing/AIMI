/**
 *
 */
package com.yeying.aimi.utils;

import android.os.Bundle;

import com.yeying.aimi.json.me.JSONException;
import com.yeying.aimi.json.me.JSONObject;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protoco.path.ResultException;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.protocol.request.Request;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author sparrow
 */
public class JSONUtil {

    // from object to json

    /**
     * @param packet
     * @return
     */
    public static Arg[] getReq(String packet, String transid) {
        String raw = packet.toString();
        Arg[] args = new Arg[2];
        args[0] = new Arg(Request.JSON, raw);
        args[1] = new Arg(Request.PATHPRAM, TransidUrlConst.getUrlByTransid(transid));
        Tools.i(raw);
        return args;
    }


    /**
     * @param packet
     * @return
     */
    public static Arg[] getReq(String packet, String transid, String token) {
        String raw = packet.toString();
        Arg[] args = new Arg[4];
        args[0] = new Arg(Request.transcode, transid);

        //加密

//		String str=null;
//		String path="/sdcard/key.txt";
//		File file=new File(path);
//		if(file.isFile() && file.exists()){
//			try {
//				//InputStreamReader read=new InputStreamReader(new FileInputStream(file),"utf-8");
//				//BufferedReader buffer=new BufferedReader(read);
//				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
//				str=reader.readLine();	
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		


//		String kpt=null;
//		try {

//			kpt=DesUtil.encrypt(packet,str);

//			kpt=DesUtil.encrypt(packet,packet);

//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

        args[1] = new Arg(Request.content, packet);
        args[2] = new Arg(Request.sessionId, token);
        Tools.i(raw);
        return args;
    }

    public static Arg[] getRe(String packet, String transid, String token) throws Exception {
//		String raw = packet.toString();
        String key = "hexinjingu_owl01";
        Arg[] args = new Arg[4];
        args[0] = new Arg(Request.transcode, transid);
        String pkt = DesUtil.encrypt(packet, key);
        args[1] = new Arg(Request.content, pkt);
        args[2] = new Arg(Request.sessionId, token);
//		Tools.i(raw);
        return args;
    }


    /**
     * @param obj
     * @return
     */
    public static JSONObject getJson(Object obj) {
        Arg[] args = getArgs(obj);
        JSONObject packet = null;
        if (args != null) {
            packet = new JSONObject();
            try {

                for (int i = 0; i < args.length; i++) {
                    packet.put(args[i].getKey(), args[i].getValue());
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        return packet;
    }

    /**
     * @param obj
     * @return
     */
    public static Arg[] getArgs(Object obj) {
        Bundle bundle = new Bundle();
        Class<?> cls = obj.getClass();

        Field[] fields = cls.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                Object value = null;
                String key = field.getName();
                Class<?> type = field.getType();
                try {
                    value = field.get(obj);
                } catch (IllegalArgumentException e) {

                    e.printStackTrace();
                } catch (IllegalAccessException e) {

                    e.printStackTrace();
                }
                if (value != null) {
                    if (type == String.class) {
                        bundle.putString(key, (String) value);
                    } else if (type == int.class) {
                        bundle.putString(key, String.valueOf(value));
                        // bundle.putInt(key,
                        // ((Integer) value).intValue());
                    } else if (type == boolean.class) {
                        bundle.putString(key, String.valueOf(value));
                        // bundle.putBoolean(key,
                        // ((Boolean)value).booleanValue());
                    } else if (type == float.class) {
                        bundle.putString(key, String.valueOf(value));
                        // bundle.putFloat(key, ((Float)value).floatValue());

                    } else if (type == double.class) {
                        bundle.putString(key, String.valueOf(value));
                        // bundle.putFloat(key, ((Double)value).floatValue());

                    }

                } else {
                    // put the value ""
                    bundle.putString(key, "");
                }
            }
        }

        return bundle2Args(bundle);
    }

    /**
     * @param mBundle
     * @return
     */
    public static Arg[] bundle2Args(Bundle mBundle) {
        int size = mBundle.size();
        String[] keys = new String[size];
        keys = mBundle.keySet().toArray(keys);
        Arg[] args = new Arg[size];
        for (int i = 0; i < size; i++) {

            args[i] = new Arg(keys[i], mBundle.getString(keys[i]));
            // Tools.i(" key:"+args[i].getKey()+"  value:"+args[i].getValue());
        }
        return args;
    }

    public static int getCount(String parent, Result result) {
        int total = 0;
        try {
            total = result.getSizeOfArray(parent);
        } catch (ResultException e) {

            e.printStackTrace();
        }
        return total;
    }

    public static int getTotal(String parent, Result result) {
        int total = 0;
        try {
            total = result.getAsInteger(parent);
        } catch (ResultException e) {

            e.printStackTrace();
        }
        return total;
    }

    public static String getField(String parent, Result result) {
        String val = null;
        try {
            val = result.getAsString(parent);
        } catch (ResultException e) {
            e.printStackTrace();
        }
        return val;
    }


    // from the json to object.
    // path header.
    public static void binding(Object obj, String parent, Result result) {
        Class<?> cls = obj.getClass();

        if (obj instanceof IComplexObject) {
            IComplexObject trend = (IComplexObject) obj;
            trend.getComplexObject(parent, result);

        } else {
            Field[] fields = cls.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    String key = field.getName();
                    String path = parent == null ? key : parent + "." + key;
                    Class<?> type = field.getType();
                    if (type == String.class) {
                        String value = null;
                        try {
                            value = result.getAsString(path);
                        } catch (ResultException e) {

                            e.printStackTrace();
                        }
                        if (value != null) {
                            try {
                                field.set(obj, value);
                            } catch (IllegalArgumentException e) {

                                e.printStackTrace();
                            } catch (IllegalAccessException e) {

                                e.printStackTrace();
                            }
                        }
                    } else if (type == int.class) {
                        Integer value = null;
                        try {
                            value = result.getAsInteger(path);
                        } catch (ResultException e) {

                            e.printStackTrace();
                        }
                        if (value != null)
                            try {
                                field.setInt(obj, value.intValue());
                            } catch (IllegalArgumentException e) {

                                e.printStackTrace();
                            } catch (IllegalAccessException e) {

                                e.printStackTrace();
                            }

                    } else if (type == boolean.class) {
                        Boolean value = null;
                        try {
                            value = result.getAsBoolean(path);
                        } catch (ResultException e) {

                            e.printStackTrace();
                        }
                        if (value != null) {
                            try {
                                field.setBoolean(obj, value);
                            } catch (IllegalArgumentException e) {

                                e.printStackTrace();
                            } catch (IllegalAccessException e) {

                                e.printStackTrace();
                            }
                        }

                    } else if (type == float.class) {
                        Float value = null;
                        try {
                            value = (float) result.getAsDouble(path);
                        } catch (ResultException e) {

                            e.printStackTrace();
                        }
                        if (value != null) {
                            try {
                                field.setFloat(obj, value);
                            } catch (IllegalArgumentException e) {

                                e.printStackTrace();
                            } catch (IllegalAccessException e) {

                                e.printStackTrace();
                            }
                        }

                    } else if (type == int[].class) {
                        int[] value = null;
                        try {
                            value = result.getAsIntegerArray(path);
                        } catch (ResultException e) {
                            e.printStackTrace();
                        }
                        if (value != null) {
                            try {
                                field.set(obj, value);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        }

                    } else if (type == double.class) {
                        Double value = null;
                        try {
                            value = result.getAsDouble(path);
                        } catch (ResultException e) {

                            e.printStackTrace();
                        }
                        if (value != null) {
                            try {
                                field.setDouble(obj, value);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (type == long.class) {
                        Long value = null;
                        try {
                            value = result.getAsLong(path);
                        } catch (ResultException e) {
                            e.printStackTrace();
                        }
                        if (value != null) {
                            try {
                                field.setLong(obj, value);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        try {
                            Object object = Class.forName(type.getName()).newInstance();
                            binding(object, key, result);

                            field.set(obj, object);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }


    public static void Reversebinding(Object obj, String parent, Result result, String parentPath) {
        Class<?> cls = obj.getClass();


        if (parentPath != null) {
            parent = parentPath + "." + parent;
        }
        String path = null;

        if (obj instanceof IComplexObject) {
            IComplexObject trend = (IComplexObject) obj;
            trend.getComplexObject(parent, result);

        } else {

            Field[] fields = cls.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {

                    String key = field.getName();
                    path = parent == null ? key : parent + "." + key;
                    Class<?> type = field.getType();
                    if (type == String.class) {
                        String value = null;
                        try {
                            value = result.getAsString(path);
                        } catch (ResultException e) {

                            e.printStackTrace();
                        }
                        if (value != null) {
                            try {
                                field.set(obj, value);
                            } catch (IllegalArgumentException e) {

                                e.printStackTrace();
                            } catch (IllegalAccessException e) {

                                e.printStackTrace();
                            }
                        }
                    } else if (type == int.class) {
                        Integer value = null;
                        try {
                            value = result.getAsInteger(path);
                        } catch (ResultException e) {

                            e.printStackTrace();
                        }
                        if (value != null)
                            try {
                                field.setInt(obj, value.intValue());
                            } catch (IllegalArgumentException e) {

                                e.printStackTrace();
                            } catch (IllegalAccessException e) {

                                e.printStackTrace();
                            }

                    } else if (type == boolean.class) {
                        Boolean value = null;
                        try {
                            value = result.getAsBoolean(path);
                        } catch (ResultException e) {

                            e.printStackTrace();
                        }
                        if (value != null) {
                            try {
                                field.setBoolean(obj, value);
                            } catch (IllegalArgumentException e) {

                                e.printStackTrace();
                            } catch (IllegalAccessException e) {

                                e.printStackTrace();
                            }
                        }

                    } else if (type == float.class) {
                        Float value = null;
                        try {
                            value = (float) result.getAsDouble(path);
                        } catch (ResultException e) {

                            e.printStackTrace();
                        }
                        if (value != null) {
                            try {
                                field.setFloat(obj, value);
                            } catch (IllegalArgumentException e) {

                                e.printStackTrace();
                            } catch (IllegalAccessException e) {

                                e.printStackTrace();
                            }
                        }

                    } else if (type == int[].class) {
                        int[] value = null;
                        try {
                            value = result.getAsIntegerArray(path);
                        } catch (ResultException e) {
                            e.printStackTrace();
                        }
                        if (value != null) {
                            try {
                                field.set(obj, value);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        }

                    } else if (type == double.class) {
                        Double value = null;
                        try {
                            value = result.getAsDouble(path);
                        } catch (ResultException e) {

                            e.printStackTrace();
                        }
                        if (value != null) {
                            try {
                                field.setDouble(obj, value);
                            } catch (IllegalArgumentException e) {

                                e.printStackTrace();
                            } catch (IllegalAccessException e) {

                                e.printStackTrace();
                            }
                        }

                    } else {
                        try {
                            Object object = Class.forName(type.getName()).newInstance();
                            Reversebinding(object, key, result, parent);

                            field.set(obj, object);

//							Object value = null;
//							try {
//								value = result.getAsObject(path);
//							} catch (ResultException e) {
//								e.printStackTrace();
//							}
//							if (value != null) {
//								try {
//									field.set(obj, value);
//								} catch (IllegalArgumentException e) {
//
//									e.printStackTrace();
//								} catch (IllegalAccessException e) {
//
//									e.printStackTrace();
//								}
//							}

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    public static <T> int bindingArrayWithoutCount(Class<T> cls, List<T> list, String parent, Result result) {
        // int total=JSONUtil.getTotal("totalcount", result);
        // total=JSONUtil.getTotal("prizelist.prizeInfo", result);
        // List<T> list=new LinkedList<T>();
        int count = JSONUtil.getCount(parent, result);
        if (count > 1) {

            // arrays=newArray(clsArray,count);

            for (int i = 0; i < count; i++) {
                try {
                    T t = cls.newInstance();

                    JSONUtil.binding(t, parent + "[" + i + "]", result);
                    list.add(t);
                } catch (IllegalAccessException e) {

                    e.printStackTrace();
                } catch (InstantiationException e) {

                    e.printStackTrace();
                }

            }
        } else if (count == 1) {

            try {
                T t = cls.newInstance();
                JSONUtil.binding(t, parent + "[0]", result);
                list.add(t);
            } catch (IllegalAccessException e) {

                e.printStackTrace();
            } catch (InstantiationException e) {

                e.printStackTrace();
            }

        }

        return count;
    }

    public static <T> int bindingArrayWithoutCount(Class<T> cls, Object[] objs, String parent, Result result) {
        int count = JSONUtil.getCount(parent, result);
        if (count > 1) {
            objs = new Object[count];
            for (int i = 0; i < count; i++) {
                try {
                    T t = (T) cls.newInstance();

                    JSONUtil.binding(t, parent + "[" + i + "]", result);
                    objs[i] = t;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        } else if (count == 1) {
            try {
                T t = (T) cls.newInstance();
                JSONUtil.binding(t, parent + "[0]", result);
                objs[0] = t;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public static <T> int bindingArray(Class<T> cls, List<T> list, String parent, Result result) {
        int total = JSONUtil.getTotal("totalcount", result);
        // total=JSONUtil.getTotal("prizelist.prizeInfo", result);
        // List<T> list=new LinkedList<T>();

        if (total > 0) {
            int count = JSONUtil.getCount(parent, result);
            // arrays=newArray(clsArray,count);

            for (int i = 0; i < count; i++) {
                try {
                    T t = cls.newInstance();
                    JSONUtil.binding(t, parent + "[" + i + "]", result);
                    list.add(t);
                } catch (IllegalAccessException e) {

                    e.printStackTrace();
                } catch (InstantiationException e) {

                    e.printStackTrace();
                }

            }
        }
        // else if(total==1){
        //
        // try {
        // T t=cls.newInstance();
        // JSONUtil.binding(t, parent, result);
        // list.add(t);
        // } catch (IllegalAccessException e) {
        //
        // e.printStackTrace();
        // } catch (InstantiationException e) {
        //
        // e.printStackTrace();
        // }
        //

        // }

        return total;
    }

    public static <T> Map<String, String> bindMap(Result result) {
        Map<String, String> resMap = new HashMap<String, String>();
        List<String> keys = result.getStringKeys();
        try {
            for (String key : keys) {
                resMap.put(key, result.getAsString(key));
            }
        } catch (ResultException e) {
            e.printStackTrace();
        }
        return resMap;
    }

}
