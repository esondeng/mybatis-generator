package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

/**
 * @author dengxiaolin
 * @since 2021/05/20
 */
public class SelectByIdsMethodGenerator extends AbstractJavaMapperMethodGenerator {


    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method("getByIds");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);

        List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
        // 只支持一个主键，不支持组合主键
        IntrospectedColumn primaryKey = introspectedColumns.get(0);
        FullyQualifiedJavaType parameterType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType type = primaryKey.getFullyQualifiedJavaType();
        parameterType.addTypeArgument(type);

        Parameter parameter = new Parameter(parameterType, primaryKey.getJavaProperty() + "s");
        parameter.addAnnotation("@Param(\"" + primaryKey.getJavaProperty() + "s" + "\")");
        method.addParameter(parameter);

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }
}
