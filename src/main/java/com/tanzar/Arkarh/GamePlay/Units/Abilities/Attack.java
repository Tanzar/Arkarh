/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tanzar.Arkarh.GamePlay.Units.Abilities;

import com.tanzar.Arkarh.Converter.Json;
import com.tanzar.Arkarh.Database.Entities.Units.UnitAbilityEntity;
import com.tanzar.Arkarh.GamePlay.Units.Abilities.Base.Trigger;
import com.tanzar.Arkarh.GamePlay.Units.Abilities.Base.UnitAbility;
import com.tanzar.Arkarh.GamePlay.Combat.Battlefield;
import com.tanzar.Arkarh.GamePlay.Combat.Side;
import com.tanzar.Arkarh.GamePlay.Units.Modifiers.Passive;
import com.tanzar.Arkarh.GamePlay.Units.Modifiers.PassiveEffect;
import com.tanzar.Arkarh.GamePlay.Units.Modifiers.Passives;
import com.tanzar.Arkarh.GamePlay.Units.TargetsSelection;
import com.tanzar.Arkarh.GamePlay.Units.EffectSchool;
import com.tanzar.Arkarh.GamePlay.Units.Unit;
import com.tanzar.Arkarh.GamePlay.Units.Units;

/**
 *
 * @author spako
 */
public class Attack extends UnitAbility{
    private final double attackDefenseBonus = 0.05;
    private final double spellPowerBonus = 0.05;
    private final double minBonus = -0.75;
    private final TargetsSelection attackStyle;
    private final int range;
    private final int areaSize;
    private final EffectSchool school;

    public Attack(UnitAbilityEntity entity) {
        super(entity, Trigger.onAction);
        String tmp = entity.getEffect();
        Json json = new Json(tmp);
        tmp = json.getString("attackStyle");
        this.attackStyle = TargetsSelection.valueOf(tmp);
        this.range = json.getInt("range");
        this.areaSize = json.getInt("areaSize");
        tmp = json.getString("school");
        this.school = EffectSchool.valueOf(tmp);
    }
    
    public Attack(Json json){
        super(json);
        this.attackStyle = TargetsSelection.valueOf(json.getString("attackStyle"));
        this.range = json.getInt("range");
        this.areaSize = json.getInt("areaSize");
        this.school = EffectSchool.valueOf(json.getString("school"));
    }
    
    @Override
    protected boolean additionalConditions(Unit source) {
        return true;
    }

    @Override
    protected Units setupTargets(Unit source, Battlefield battlefield) {
        Side side = battlefield.getOppositeSide(source);
        int position = source.getPosition();
        Units targets = TargetsSelection.getTargets(attackStyle, position, side, range, areaSize);
        return targets;
    }
    
    @Override
    protected void onUse(Unit source, Units targets) {
        Unit[] units = targets.toArray();
        for(Unit target: units){
            this.attackTarget(source, target);
        }
    }
    
    private void attackTarget(Unit source, Unit target){
        int totalDamage = this.baseAttack(source, target);
        this.lifeSteal(source, target, totalDamage);
        this.bonusDamage(source, target);
    }
    
    private int baseAttack(Unit source, Unit target){
        int damage = source.getTotalBaseDamage();
        int attack = source.getTotalAttack();
        int spellPower = source.getTotalSpellPower();
        int totalDamage = 0;
        if(this.school.equals(EffectSchool.physical)){
            totalDamage = this.physicalAttack(source, target, damage, attack);
        }
        else{
            if(this.school.equals(EffectSchool.none)){
                totalDamage = 0;
            }
            else{
                totalDamage = this.magicAttack(source, target, damage, spellPower);
            }
        }
        if(totalDamage <= 0){
            totalDamage = 1;
        }
        target.takeDamage(totalDamage);
        this.report(source, target, totalDamage, school);
        return totalDamage;
    }
    
    private int physicalAttack(Unit source, Unit target, int damage, int attack){
        double attackDefenseMultiplier = this.calculateAttackDefenseMultiplier(source, target, attack);
        double weaknessMultiplier = this.weaknessMultiplier(target, EffectSchool.physical);
        double armorMultiplier = this.armorMultiplier(source, target);
        damage = (int) Math.round(damage * attackDefenseMultiplier * weaknessMultiplier * armorMultiplier);
        return damage;
    }
    
    private double calculateAttackDefenseMultiplier(Unit source, Unit target, int attack){
        int defense = this.calculateTargetDefense(source, target);
        double multiplier = (attack - defense) * this.attackDefenseBonus;
        if(multiplier < this.minBonus){
            return 1 + this.minBonus;
        }
        else{
            return 1 + multiplier;
        }
    }
    
    private int calculateTargetDefense(Unit source, Unit target){
        double multiplier = this.calculateIgnore(source, PassiveEffect.ignoreDefensePercentage);
        int defense = (int) Math.round(target.getTotalDefense() * multiplier);
        return defense;
    }
    
    private int magicAttack(Unit source, Unit target, int damage, int spellPower){
        double spellPowerMultiplier = 1 + (spellPower * this.spellPowerBonus);
        double weaknessMultiplier = this.weaknessMultiplier(target, this.school);
        double wardMultiplier = this.wardMultiplier(source, target);
        damage = (int) Math.round(damage * spellPowerMultiplier);
        damage = (int) Math.round(damage * weaknessMultiplier);
        damage = (int) Math.round(damage * wardMultiplier);
        return damage;
    }
    
    private void bonusDamage(Unit source, Unit target){
        Passives sourcePassives = source.getPassives();
        Passives bonusDamageEffects = sourcePassives.getByEffect(PassiveEffect.bonusDamage, EffectSchool.none);
        for(Passive passive : bonusDamageEffects.toArray()){
            this.dealBonusDamage(source, target, passive);
        }
    }
    
    private void dealBonusDamage(Unit source, Unit target, Passive passive){
        EffectSchool school = passive.getSchool();
        int damage = passive.getTotalValue();
        if(school.equals(EffectSchool.physical)){
            damage = (int) Math.round(damage * this.armorMultiplier(source, target) * this.weaknessMultiplier(target, school));
        }
        else{
            damage = (int) Math.round(damage * this.wardMultiplier(source, target) * this.weaknessMultiplier(target, school));
        }
        target.takeDamage(damage);
        this.report(source, target, damage, school);
        this.lifeSteal(source, target, damage);
    }
    
    private double weaknessMultiplier(Unit target, EffectSchool attackSchool){
        int totalWeaknessToAttack = target.getPassiveValue(PassiveEffect.weakness, attackSchool);
        double weakness = Math.round(totalWeaknessToAttack / 100);
        if(weakness < 0){
            return 1.0;
        }
        else{
            return 1.0 + weakness;
        }
    }
    
    private double armorMultiplier(Unit source, Unit target){
        double armor = target.getTotalArmor();
        double multiplier = this.calculateIgnore(source, PassiveEffect.ignoreArmorPercentage);
        double value = (armor * multiplier) / 100;
        value = 1 - value;
        return value;
    }
    
    private double wardMultiplier(Unit source, Unit target){
        double ward = target.getTotalWard();
        double multiplier = this.calculateIgnore(source, PassiveEffect.ignoreWardPercentage);
        double value = (ward * multiplier) / 100;
        value = 1 - value;
        return value;
    }
    
    private double calculateIgnore(Unit source, PassiveEffect ignore){
        Passives passives = source.getPassives();
        int value = passives.summarizeValues(ignore, EffectSchool.none);
        if(value > 100){
            value = 100;
        }
        if(value < 0){
            value = 0;
        }
        double multiplier = 1 - (value/100);
        return multiplier;
    }
    
    private void lifeSteal(Unit source, Unit target, int damage){
        double percentage = source.getPassiveValue(PassiveEffect.lifeSteal, EffectSchool.none);
        if(percentage > 0){
            double lifestealMultiplier = percentage / 100;
            double stolenHealth = ((double) damage) * lifestealMultiplier;
            source.heal((int) stolenHealth);
            String text = source.toString() + " steals " + ((int) stolenHealth) + " health from " + target;
            this.report.abilityUse(this, source, target, text);
        }
    }
    
    private void report(Unit source, Unit target, int value, EffectSchool school){
        String stringFormat = source.toString() + " attacks " + target.toString() + " for " + value + " " + school + " damage.";
        this.report.abilityUse(this, source, target, stringFormat);
    }

    @Override
    protected void formJson(Json json) {
        json.add("attackStyle", this.attackStyle.toString());
        json.add("range", this.range);
        json.add("areaSize", this.areaSize);
        json.add("school", this.school.toString());
    }
    
}
