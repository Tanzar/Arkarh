/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tanzar.Arkarh.GamePlay.Units.Modifiers;

import com.tanzar.Arkarh.Converter.Json;
import com.tanzar.Arkarh.GamePlay.Units.EffectSchool;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author spako
 */
public class Passives {
    private List<Passive> passives;
    
    public Passives(){
        this.passives = new ArrayList<Passive>();
    }
    
    public Passives(Json json){
        this.passives = new ArrayList<Passive>();
        String[] items = json.getStringArray("passives");
        for(String item: items){
            Passive passive = new Passive(item);
            this.add(passive);
        }
    }
    
    public Passives(String jsonString){
        this.passives = new ArrayList<Passive>();
        Json json = new Json(jsonString);
        String[] items = json.getStringArray("passives");
        for(String item: items){
            Passive passive = new Passive(item);
            this.add(passive);
        }
    }
    
    public Passives(Passives template){
        this.passives = new ArrayList<Passive>();
        Passive[] templates = template.toArray();
        for(Passive passive: templates){
            Passive copy = new Passive(passive);
            this.passives.add(copy);
        }
    }
    
    public void add(Passive passive){
        if(passive != null){
            Passive found = null;
            for(Passive item: this.passives){
                if(item.equals(passive)){
                    found = item;
                    break;
                }
            }
            if(found == null){
                this.passives.add(passive);
            }
            else{
                int stacks = passive.getStacks();
                found.addStacks(stacks);
            }
        }
    }
    
    public Passive get(int index){
        return this.passives.get(index);
    }
    
    public void remove(int index){
        this.passives.remove(index);
    }
    
    public void remove(Passive item){
        this.passives.remove(item);
    }
    
    public int size(){
        return this.passives.size();
    }
    
    public Passives getByEffect(PassiveEffect effect, EffectSchool school){
        Passives results = new Passives();
        for(Passive passive: this.passives){
            if(effect == PassiveEffect.weakness){
                if(passive.getSchool().equals(school)){
                    results.add(passive);
                }
            }
            else{
                if(passive.getEffect().equals(effect)){
                    results.add(passive);
                }
            }
        }
        return results;
    }
    
    public int summarizeValues(PassiveEffect effect, EffectSchool school){
        int value = 0;
        for(Passive passive: this.passives){
            if(effect == PassiveEffect.weakness && school != EffectSchool.none){
                if(passive.getSchool().equals(school)){
                    value += Math.abs(passive.getTotalValue());
                }
            }
            else{
                if(passive.getEffect().equals(effect)){
                    value += passive.getTotalValue();
                }
            }
        }
        return value;
    }
    
    public Passive[] toArray(){
        Passive[] array = new Passive[this.size()];
        this.passives.toArray(array);
        return array;
    }
}
