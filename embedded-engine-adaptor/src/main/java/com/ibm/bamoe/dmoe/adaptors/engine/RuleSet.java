package com.ibm.bamoe.dmoe.adaptors.engine;

import lombok.Data;

@Data
public class RuleSet {

    private String knowledgeSessionID;
    private String ruleFlowID;
    private String ruleFlowGroup;
    private MavenCoordinates mavenCoordinates;
}