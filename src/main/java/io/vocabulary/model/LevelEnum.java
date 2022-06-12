package io.vocabulary.model;

import lombok.Getter;

/**
 *
 * @author Ashin
 * @date 2022-05
 * @Desc
 */
@Getter
public enum LevelEnum {
//    LEVEL4("level4","http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKXEPiAO7apFDFkbooe44g224.zip"),
    LEVEL4("level4","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/01/14d93f044922488898841f7beb81e2edlevel4.zip"),
//    RESEARCH("research","http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKXHmeAKEkaF5WpvsCqY8s385.zip"),
    RESEARCH("research","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/02/9fd9a87ca9394e4b9a5bc4d135c3620dresearch.zip"),
//    MIDDLE("middle","http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKXHpmALX6aDXBD9hdgbEQ538.zip"),
    MIDDLE("middle","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/02/08aa54e2a935493a8d13e3b3aa646097middle.zip"),
//    LEVEL6("level6","http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKXHNqAK0xuJvuCxeAZr3I450.zip"),
    LEVEL6("level6","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/02/66dd639688994e849be29f5027c6299elevel6.zip"),
//    SPECIAL4("special4","http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKXHwiARPDaGuLCZg3cVCY374.zip"),
    SPECIAL4("special4","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/02/74c81027eda444b498a3059018afd0c9special4.zip"),
//    HIGH("high","http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKXH0SAFm41FhyKjywt8FA554.zip"),
    HIGH("high","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/02/a224049467c64dcfb4c672695626d959high.zip"),
//    IELTS("ielts","http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKXHVeAYCnZEbjFKiNcT00043.zip"),
    IELTS("ielts","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/02/83102f45c5f7464aae1e8a00ed0b8635ielts.zip"),
//    SPECIAL8("special8","http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKXH4CAYP9JC6JP8gEPP2Q803.zip"),
    SPECIAL8("special8","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/02/2ac99f79da804383aeeed80832a784dbspecial8.zip"),
//    LIBERTY("liberty","http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKXH8eAfvkkGweDESMEv80923.zip"),
    LIBERTY("liberty","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/02/e1d6ba47cbea4c8faf548003ee486cd5liberty.zip"),
//    ESCALATE("escalate","http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKXIGOAbG8-Fuf0sOSBoYU608.zip"),
    ESCALATE("escalate","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/02/4b3fa28d39c243f2988454d17c4c7e4fescalate.zip"),
    TOEFL("toefl","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/02/3352c07f505c44209d16ff5df51afa9btoefl.zip"),
//    LEVELFOUR("levelfour","http://8.136.140.130:18080/group1/M00/00/00/rB_1LWKXIJ2AU_KtE3cLFHiUk_M944.zip");
    LEVELFOUR("levelfour","https://guli-10010.oss-cn-beijing.aliyuncs.com/2022/06/02/1b67e1dd8029489cb61682f802c701fdlevelfour.zip");

    private String code;
    private String url;
    LevelEnum(String code,String url) {

        this.code = code;
        this.url = url;
    }
}
