/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

import com.example.mapdemo.activity.MapaDasLocalizacoesActivity;
import com.example.mapdemo.activity.NovaLocalizacaoComFotoActivity;

/**
 * A list of all the demos we have available.
 */
public final class DemoDetailsList {

    /** This class should not be instantiated. */
    private DemoDetailsList() {
    }

    public static final DemoDetails[] DEMOS = {
            new DemoDetails(R.string.nova_localizacao,
                    R.string.nova_localizacao_label,
                    NovaLocalizacaoComFotoActivity.class),
            new DemoDetails(R.string.localizacoes_mapa,
                    R.string.localizacoes_mapa_label,
                    MapaDasLocalizacoesActivity.class)

    };
}
