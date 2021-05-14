/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
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
package cn.fisok.sqloy.serialnum.consts;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 流水号生成器类型
 */
public interface GeneratorType {
    String SNOW_FLAKE = "SN_GENRATOR_SNOW_FLAKE";
    String AUTO_INCREMENT = "SN_GENRATOR_AUTO_INCREMENT";
    String AUTO_INCREMENT36_WITH_RANDOM = "SN_GENRATOR_AUTO_INCREMENT36_WITH_RANDOM";
    String UUID = "SN_GENRATOR_UUID";
    String NANO_TIME36 = "SN_GENRATOR_NANO_TIME36";
    String DATE_CYCLE_Y = "SN_GENRATOR_DATE_CYCLE_Y";
    String DATE_CYCLE_YM = "SN_GENRATOR_DATE_CYCLE_YM";
    String DATE_CYCLE_YMD = "SN_GENRATOR_DATE_CYCLE_YMD";
}
