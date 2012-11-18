/**
 * Copyright 2011 Adrian Witas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.abstractmeta.code.g.core.config.jpa;

import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.core.jpa.DbConnection;
import org.abstractmeta.code.g.core.util.DecoderUtil;
import org.abstractmeta.code.g.core.util.StringUtil;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Decodes Jpa configuration from descriptor.
 *
 * @author Adrian Witas
 */
public class JpaEntityGeneratorConfigurationDecoder {


    public JpaEntityGeneratorConfiguration decode(Descriptor descriptor) {
        if (descriptor == null) {
            throw new IllegalArgumentException("descriptor was null");
        }
        JpaEntityGeneratorConfiguration result = new JpaEntityGeneratorConfiguration();
        @SuppressWarnings("unchecked")
        Map<String, String> options = Map.class.cast(descriptor.getOptions());

        try {
            result.setConnection(decodeDbConnection(options));
            result.setEntityPostfix(descriptor.getTargetPostfix());
            result.setEntityPrefix(descriptor.getTargetPrefix());
            if(descriptor.getTargetPackage() == null) {
                throw new IllegalStateException("targetPackage was null");
            }
            result.setTargetPackage(descriptor.getTargetPackage());

            String tableMatchingPattern = options.get("tableMatchingPattern");
            boolean isTableMatchingPattern = StringUtil.isNotEmpty(tableMatchingPattern);
            String tables = options.get("tables");
            boolean isTables = StringUtil.isNotEmpty(tables);
            String sql = options.get("sql");
            boolean isSql = StringUtil.isNotEmpty(sql);
            if (isTableMatchingPattern) {
                result.setTableMatchingPattern(Pattern.compile(tableMatchingPattern));
            }
            if (isTables) {
                result.setTablesNames(DecoderUtil.readStringList(options, "tables"));
            } else if (isSql) {
                result.setSql(sql);
            } else if (!isTables) {
                throw new IllegalStateException("missing source option tables, tableMatchingPattern or sql configuration option");
            }
            result.setTypeMapping(DecoderUtil.matchWithPrefixAsIntegerClassMap(options, "typeMapping"));
            if(isSql) {
                result.setSqlName(DecoderUtil.readStringRequired(options, "sqlName"));
            }

        } catch (RuntimeException e) {
            throw new IllegalStateException("Failed to load JPA configuration for descriptor " + descriptor.getPlugin(), e);
        }
        return result;
    }


    protected DbConnection decodeDbConnection(Map<String, String> options) {
        DbConnection result = new DbConnection();
        result.setUrl(DecoderUtil.readStringRequired(options, "url"));
        result.setUsername(options.get("username"));
        result.setPassword(options.get("password"));
        result.setDriverClassName(DecoderUtil.readClassRequired(options, "driverClassName"));
        return result;
    }

}
