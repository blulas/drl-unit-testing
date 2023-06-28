package com.ibm.bamoe.dmoe.testing;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

import com.ibm.bamoe.dmoe.adaptors.engine.RuleSet;
import com.ibm.bamoe.dmoe.adaptors.engine.RuleSetResult;
import com.ibm.bamoe.dmoe.adaptors.engine.KnowledgeContainer;
import com.ibm.bamoe.dmoe.adaptors.engine.RuleEngineAdaptor;

@Data
public class RulesTestingContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesTestingContext.class);

    private RuleSetResult response = null;
    private RuleEngineAdaptor ruleEngineAdaptor = new RuleEngineAdaptor();
    private Map<String, Object> facts = new ConcurrentHashMap<String, Object>(); 
    private Map<String, Object> globals = new ConcurrentHashMap<String, Object>(); 
    private String dataFileLocation;
    private String loggingLevel = "DEBUG";

    public void addFact(String name, Object fact) {
        getFacts().put(name, fact);
    }

    public void addFact(Object fact) {

        // Use the class name as the fact name
        char c[] = fact.getClass().getSimpleName().toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        String name = new String(c);

        // Add a unique ID to the name, just to make sure
        String uniqueName = name + "-" + UUID.randomUUID().toString();
        getFacts().put(uniqueName, fact);
    }

    public Object getFact(String name) {
        return facts.get(name);
    }

    public void addGlobal(String name, Object var) {
        getFacts().put(name, var);
    }

    public void addGlobal(Object var) {

        // Use the class name as the var name
        char c[] = var.getClass().getSimpleName().toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        String name = new String(c);

        // Add a unique ID to the name, just to make sure
        String uniqueName = name + "-" + UUID.randomUUID().toString();
        getGlobals().put(uniqueName, var);
    }

    public Object getGlobal(String name) {
        return globals.get(name);
    }
}
