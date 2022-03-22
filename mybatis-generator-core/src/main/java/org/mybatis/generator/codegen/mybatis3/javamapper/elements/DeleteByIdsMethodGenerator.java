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
 * @since 2021/05/21
 */
public class DeleteByIdsMethodGenerator extends AbstractJavaMapperMethodGenerator {
    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        Method method = new Method(introspectedTable.getDeleteByIdsStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());

        List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
        // only support one column primary key
        IntrospectedColumn primaryKey = introspectedColumns.get(0);
        importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));

        StringBuilder sb = new StringBuilder();
        FullyQualifiedJavaType type = primaryKey.getFullyQualifiedJavaType();
        importedTypes.add(type);

        String paramName = primaryKey.getJavaProperty() + "s";
        FullyQualifiedJavaType parameterType = FullyQualifiedJavaType.getNewListInstance();
        parameterType.addTypeArgument(type);
        Parameter parameter = new Parameter(parameterType, paramName);

        sb.append("@Param(\"");
        sb.append(paramName);
        sb.append("\")");
        parameter.addAnnotation(sb.toString());

        method.addParameter(parameter);


        boolean hasLastUpdater = introspectedTable.getColumn("last_updater").isPresent();
        if(hasLastUpdater){
            IntrospectedColumn lastUpdater = introspectedTable.getColumn("last_updater").get();
            Parameter lastUpdaterParameter = new Parameter(lastUpdater.getFullyQualifiedJavaType(), lastUpdater.getJavaProperty());
            sb.setLength(0);
            sb.append("@Param(\"");
            sb.append(lastUpdater.getJavaProperty());
            sb.append("\")");
            lastUpdaterParameter.addAnnotation(sb.toString());
            method.addParameter(lastUpdaterParameter);
        }

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }
}
