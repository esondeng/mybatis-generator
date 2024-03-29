/*
 *    Copyright 2006-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

public class DeleteByPrimaryKeyElementGenerator extends
        AbstractXmlElementGenerator {

    private final boolean isSimple;

    public DeleteByPrimaryKeyElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        boolean hasValidColumn = introspectedTable.getColumn("valid").isPresent();
        boolean hasUsableColumn = introspectedTable.getColumn("is_usable").isPresent();
        boolean hasUsableStateColumn = introspectedTable.getColumn("usable_state").isPresent();

        if (!hasValidColumn && !hasUsableColumn && !hasUsableStateColumn) {
            return;
        }

        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getDeleteByPrimaryKeyStatementId())); //$NON-NLS-1$
        String parameterClass;
        if (!isSimple && introspectedTable.getRules().generatePrimaryKeyClass()) {
            parameterClass = introspectedTable.getPrimaryKeyType();
        }
        else {
            // PK fields are in the base class. If more than on PK
            // field, then they are coming in a map.
            if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
                parameterClass = "map"; //$NON-NLS-1$
            }
            else {
                parameterClass = introspectedTable.getPrimaryKeyColumns()
                        .get(0).getFullyQualifiedJavaType().toString();
            }
        }
        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterClass));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());

        boolean hasLastUpdater = introspectedTable.getColumn("last_updater").isPresent();
        boolean hasLastUpdateTime = introspectedTable.getColumn("last_update_time").isPresent();

        if (hasValidColumn) {
            sb.append(" set valid = 0, ");
        }

        if (hasUsableColumn) {
            sb.append(" set is_usable = 0, ");
        }

        if (hasUsableStateColumn) {
            sb.append(" set usable_state = 0, ");
        }

        if (hasLastUpdater) {
            String jdbcType = introspectedTable.getColumn("last_updater").get().getJdbcTypeName();
            sb.append("hasLastUpdater = #{hasLastUpdater,jdbcType=").append(jdbcType).append("}, ");
        }

        if (hasLastUpdateTime) {
            sb.append("last_update_time = now()");
        }
        else {
            sb.append("update_time = now()");
        }

        answer.addElement(new TextElement(sb.toString()));

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            }
            else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
        }

        if (hasValidColumn) {
            sb.append(" and valid = 1");
        }
        if (hasUsableColumn) {
            sb.append(" and is_usable = 1");
        }
        if (hasUsableStateColumn) {
            sb.append(" and usable_state = 1");
        }

        answer.addElement(new TextElement(sb.toString()));

        if (context.getPlugins()
                .sqlMapDeleteByPrimaryKeyElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
            parentElement.addElement(new TextElement(""));
        }
    }
}
