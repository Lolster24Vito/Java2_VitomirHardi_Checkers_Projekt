package hr.algebra.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static void readClassInfo(Class<?> clazz, StringBuilder classInfo) {
        classInfo.append("<div class=\"card\">");
        classInfo.append("<div class=\"card-header\">");
        classInfo.append("<h2>");
         readClassHeader(clazz,classInfo);
        appendParent(clazz, classInfo, true);
        appendInterfaces(clazz, classInfo);
        classInfo.append("</h2>");

        HtmlStringUtils.wrapInTags("<p>",clazz.getPackage().toString(),"</p>",classInfo);
        classInfo.append("</div>");
        // appendPackage(clazz, classInfo);
        //konstructors
        appendConstructors(clazz,classInfo);
        appendMethods(clazz, classInfo);

        appendFields(clazz, classInfo);
classInfo.append("</div>\n");
        classInfo.append("</br></br>");

    }

    private static void readClassHeader(Class<?> clazz,StringBuilder classInfo){

        classInfo.append(Modifier.toString(clazz.getModifiers()));
        classInfo.append(" ").append(clazz.getSimpleName()).append(" ");

    }

    private static void appendPackage(Class<?> clazz, StringBuilder classInfo) {
        HtmlStringUtils.wrapInTags("<p>",clazz.getPackage().toString(),"</p>",classInfo);

    }



    private static void appendParent(Class<?> clazz, StringBuilder classInfo, boolean first) {
        Class<?> parent = clazz.getSuperclass();
        if(parent == null||parent==Object.class) {
            return;
        }
        if (first) {
            classInfo
                    .append(System.lineSeparator())
                    .append("extends");
        }
        classInfo
                .append(" ")
                .append(parent.getName());
        appendParent(parent, classInfo, false);
    }

    private static void appendInterfaces(Class<?> clazz, StringBuilder classInfo) {
        if (clazz.getInterfaces().length > 0) {
            classInfo
                    .append(System.lineSeparator())
                    .append("implements ")
                    .append(
                            Arrays.stream(clazz.getInterfaces())
                                    .map(Class::getSimpleName)
                                    .collect(Collectors.joining(" "))
                    );
        }
    }

    public static void readClassAndMembersInfo(Class<?>clazz, StringBuilder classAndMembersInfo) {
        readClassInfo(clazz, classAndMembersInfo);
        appendFields(clazz, classAndMembersInfo);
        appendMethods(clazz, classAndMembersInfo);
        appendConstructors(clazz, classAndMembersInfo);
    }

    private static void appendFields(Class<?> clazz, StringBuilder classAndMembersInfo) {
        boolean firstLoop=true;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields){
            if(firstLoop){
                firstLoop=false;
                HtmlStringUtils.wrapInTags("<h4>","Fields","</h4>",classAndMembersInfo);
                classAndMembersInfo.append("<ul>");
            }
             HtmlStringUtils.wrapInTags("<li>",Modifier.toString(field.getModifiers())+" "+field.getType().getSimpleName()+" "+field.getName(),"</li>",classAndMembersInfo);

        }
        if(!firstLoop)
            classAndMembersInfo.append("</ul><hr>");

    }

    private static void appendMethods(Class<?> clazz, StringBuilder classAndMembersInfo) {
        boolean firstLoop=true;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if(firstLoop){
                firstLoop=false;
                HtmlStringUtils.wrapInTags("<h4>","Methods","</h4>",classAndMembersInfo);
                classAndMembersInfo.append("<ul>");
            }
            classAndMembersInfo.append("<li>");
            appendAnnotations(method, classAndMembersInfo);
            classAndMembersInfo
                    .append(Modifier.toString(method.getModifiers()))
                    .append(" ")
                    .append(method.getReturnType())
                    .append(" ")
                    .append(method.getName());
            appendParameters(method, classAndMembersInfo);
            appendExceptions(method, classAndMembersInfo);
            classAndMembersInfo.append("</li>");

        }
        if(!firstLoop)
            classAndMembersInfo.append("</ul><hr>");
    }
    private static void appendAnnotations(Executable executable, StringBuilder classAndMembersInfo) {


        classAndMembersInfo.append(
                Arrays.stream(executable.getAnnotations())
                        .map(Objects::toString)
                        .collect(Collectors.joining(System.lineSeparator())));
    }
    private static void appendParameters(Executable executable, StringBuilder classAndMembersInfo) {
        classAndMembersInfo.append("(");
        boolean firstLoop=true;
        for (Parameter parameter : executable.getParameters()) {
            if(firstLoop){
                firstLoop=false;
            }
            else  classAndMembersInfo.append(",");

            classAndMembersInfo.append(parameter.getType().getSimpleName()).append(" ");
            classAndMembersInfo.append(parameter.getName());

        }
        classAndMembersInfo.append(")");
    }
    private static void appendExceptions(Executable executable, StringBuilder classAndMembersInfo) {
        if (executable.getExceptionTypes().length > 0) {
            classAndMembersInfo.append(" throws ");
            classAndMembersInfo.append(
                    Arrays.stream(executable.getExceptionTypes())
                            .map(Class::getName)
                            .collect(Collectors.joining(" "))
            );
        }
    }

    private static void appendConstructors(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        boolean firstLoop=true;
        for (Constructor constructor : constructors) {
            if(firstLoop){
                firstLoop=false;
                HtmlStringUtils.wrapInTags("<h4>","Constructors","</h4>",classAndMembersInfo);
                classAndMembersInfo.append("<ul>");
            }
            classAndMembersInfo.append("<li>");
            appendAnnotations(constructor, classAndMembersInfo);
            classAndMembersInfo
                    .append(Modifier.toString(constructor.getModifiers()))
                    .append(" ")
                    .append(constructor.getName());
            appendParameters(constructor, classAndMembersInfo);
            appendExceptions(constructor, classAndMembersInfo);
            classAndMembersInfo.append("</li>");

        }
        if(!firstLoop)
        classAndMembersInfo.append("</ul><hr>");

    }
}
