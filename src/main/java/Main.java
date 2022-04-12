import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;


public class Main {
    public static void main(String[] args) throws Exception{
        Class cas = ToCheck.class;
        Object testObj = cas.newInstance();
        Method[] methods = cas.getDeclaredMethods();
        ArrayList<Method> arrLi = new ArrayList<>();
        Method beforeMethod = null;
        Method afterMethod = null;
        for (Method o : cas.getDeclaredMethods()) {
            if (o.isAnnotationPresent(Test.class)) {
                arrLi.add(o);
            }
            if (o.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeMethod == null) beforeMethod = o;
                else throw new RuntimeException("Больше одного метода с аннотацией BeforeSuite");
            }
            if (o.isAnnotationPresent(AfterSuite.class)) {
                if (afterMethod == null) afterMethod = o;
                else throw new RuntimeException("Больше одного метода с аннотацией AfterSuite");
            }
            arrLi.sort(new Comparator<Method>() {
                @Override
                public int compare(Method o1, Method o2) {
                    return o2.getAnnotation(Test.class).priority()-o1.getAnnotation(Test.class).priority();
                }
            });
        }

        if (beforeMethod != null) beforeMethod.invoke(testObj, null);
        for (Method o : arrLi) o.invoke(testObj, null);
        if (afterMethod != null) afterMethod.invoke(testObj, null);
    }

}


