/*
 *     Copyright 2024 Siroshun09
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package dev.siroshun.codec4j.io.yaml;


import org.jetbrains.annotations.NotNullByDefault;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import static org.yaml.snakeyaml.DumperOptions.FlowStyle.BLOCK;
import static org.yaml.snakeyaml.DumperOptions.FlowStyle.FLOW;

@NotNullByDefault
public record YamlParameter(
        DumperOptions.FlowStyle defaultFlowStyle,
        DumperOptions.FlowStyle sequenceFlowStyle,
        DumperOptions.FlowStyle mapFlowStyle,
        DumperOptions.ScalarStyle scalarStyle,
        int indent
) {

    @Override
    public DumperOptions.FlowStyle sequenceFlowStyle() {
        return this.defaultFlowStyle == BLOCK ? this.sequenceFlowStyle : FLOW;
    }

    @Override
    public DumperOptions.FlowStyle mapFlowStyle() {
        return this.defaultFlowStyle == BLOCK ? this.mapFlowStyle : FLOW;
    }

    Yaml createYaml() {
        var dumperOptions = this.createDumperOptions();
        var loaderOptions = this.createLoaderOptions();
        var constructor = this.createConstructor(loaderOptions);
        var representer = this.createRepresenter(dumperOptions);

        return new Yaml(constructor, representer, dumperOptions, loaderOptions);
    }

    private LoaderOptions createLoaderOptions() {
        var loaderOptions = new LoaderOptions();

        loaderOptions.setCodePointLimit(Integer.MAX_VALUE);
        loaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);

        return loaderOptions;
    }

    private DumperOptions createDumperOptions() {
        var dumperOptions = new DumperOptions();

        dumperOptions.setDefaultFlowStyle(this.defaultFlowStyle);
        dumperOptions.setDefaultScalarStyle(this.scalarStyle);
        dumperOptions.setIndent(this.indent);

        return dumperOptions;
    }

    ObjectConstructor createConstructor() {
        return this.createConstructor(this.createLoaderOptions());
    }

    private ObjectConstructor createConstructor(LoaderOptions loaderOptions) {
        return new ObjectConstructor(loaderOptions);
    }

    Representer createRepresenter() {
        return this.createRepresenter(this.createDumperOptions());
    }

    private Representer createRepresenter(DumperOptions dumperOptions) {
        var representer = new Representer(dumperOptions);
        representer.setDefaultFlowStyle(this.defaultFlowStyle);
        representer.setDefaultScalarStyle(this.scalarStyle);
        return representer;
    }
}
