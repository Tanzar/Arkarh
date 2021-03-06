/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tanzar.Arkarh.GamePlay.Units.Abilities.Base;

import com.tanzar.Arkarh.Converter.Json;
import com.tanzar.Arkarh.Database.Entities.Units.UnitAbilityEntity;
import com.tanzar.Arkarh.GamePlay.Units.Abilities.*;

/**
 *
 * @author Tanzar
 */
public class AbilityFactory {
    
    public static UnitAbility convert(Json json){
        String groupString = json.getString("group");
        UnitAbilityGroup group = UnitAbilityGroup.valueOf(groupString);
        switch(group){
            case attack:
                return new Attack(json);
            case heal:
                return new Heal(json);
            case buff:
                return new Buff(json);
            case reincarnate:
                return new Reincarnate(json);
            case ressurect:
                return new Ressurect(json);
            case demoralize:
                return new Demoralize(json);
            case summon:
                return new Summon(json);
            case necromancy:
                return new Necromancy(json);
            case regeneration:
                return new Regeneration(json);
            default:
                return null;
        }
    }
    
    public static UnitAbility convertAbility(UnitAbilityEntity entity){
        String groupString = entity.getEffectGroup();
        UnitAbilityGroup group = UnitAbilityGroup.valueOf(groupString);
        switch(group){
            case attack:
                return new Attack(entity);
            case heal:
                return new Heal(entity);
            case buff:
                return new Buff(entity);
            case reincarnate:
                return new Reincarnate(entity);
            case ressurect:
                return new Ressurect(entity);
            case demoralize:
                return new Demoralize(entity);
            case summon:
                return new Summon(entity);
            case necromancy:
                return new Necromancy(entity);
            case regeneration:
                return new Regeneration(entity);
            default:
                return null;
        }
    }
    
    public static UnitAbility newAbility(UnitAbilityGroup group){
        switch(group){
            case attack:
                return null;
            case heal:
                return new Heal();
            case buff:
                return new Buff();
            case reincarnate:
                return new Reincarnate();
            case ressurect:
                return new Ressurect();
            case demoralize:
                return new Demoralize();
            case summon:
                return new Summon();
            case necromancy:
                return new Necromancy();
            case regeneration:
                return new Regeneration();
            default:
                return null;
        }
    }
    
}
