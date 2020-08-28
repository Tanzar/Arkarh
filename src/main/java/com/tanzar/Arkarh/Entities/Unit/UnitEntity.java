/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tanzar.Arkarh.Entities.Unit;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author spako
 */
@Entity
@Table(name = "units")
public class UnitEntity implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Integer id;
    
    @Column(name = "unit_name")
    private String unitName;
    
    @Column(name = "asset_name")
    private String assetName;
    
    @Column(name = "fraction")
    private String fraction;
    
    @Column(name = "role")
    private String role;
    
    @Column(name = "tier")
    private String tier;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "attack")
    private int attack;
    
    @Column(name = "spell_power")
    private int spellPower;
    
    @Column(name = "effect_type")
    private String effectType;
    
    @Column(name = "damage")
    private int damage;
    
    @Column(name = "healing")
    private int healing;
    
    @Column(name = "attack_type")
    private String attackType;
    
    @Column(name = "defense")
    private int defense;
    
    @Column(name = "armor")
    private int armor;
    
    @Column(name = "ward")
    private int ward;
    
    @Column(name = "health")
    private int health;
    
    @Column(name = "upkeep")
    private int upkeep;
    
    @Column(name = "speed")
    private int speed;
    
    @Column(name = "range")
    private int range;
    
    @Column(name = "morale")
    private int morale;

    public UnitEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return unitName;
    }

    public void setName(String name) {
        this.unitName = name;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
    
    public String getFraction(){
        return this.fraction;
    }
    
    public void setFraction(String fraction){
        this.fraction = fraction;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getSpellPower() {
        return spellPower;
    }

    public void setSpellPower(int spellPower) {
        this.spellPower = spellPower;
    }

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHealing() {
        return healing;
    }

    public void setHealing(int healing) {
        this.healing = healing;
    }

    public String getAttackType() {
        return attackType;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getWard() {
        return ward;
    }

    public void setWard(int ward) {
        this.ward = ward;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getUpkeep() {
        return upkeep;
    }

    public void setUpkeep(int upkeep) {
        this.upkeep = upkeep;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getMorale() {
        return morale;
    }

    public void setMorale(int morale) {
        this.morale = morale;
    }
}