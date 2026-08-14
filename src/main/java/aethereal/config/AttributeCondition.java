package aethereal.config;


import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public record AttributeCondition(RegistryEntry<EntityAttribute> a, EntityAttributeModifier.Operation b, double c) {
    public AttributeCondition {
        if (a == null) {
            throw new IllegalArgumentException("Attribute cannot be null");
        }
    }

    public AttributeCondition(RegistryEntry<EntityAttribute> attribute, double expectedAmount, EntityAttributeModifier.Operation expectedOperation) {
        this(attribute, expectedOperation, expectedAmount);
    }

    @Override
    public RegistryEntry<EntityAttribute> a() {
        return this.a;
    }

    @Override
    public EntityAttributeModifier.Operation b() {
        return this.b;
    }

    @Override
    public double c() {
        return this.c;
    }

    public boolean a(RegistryEntry<EntityAttribute> currentAttr, EntityAttributeModifier modifier) {
        return currentAttr.equals(this.a) && Math.abs(modifier.value() - this.c) < 9.9999942618434E-4d && modifier.operation() == this.b;
    }

    public boolean a(ItemStack stack) {
        AttributeModifiersComponent modifiersComponent = stack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
        for (AttributeModifiersComponent.Entry entry : modifiersComponent.modifiers()) {
            if (a(entry.attribute(), entry.modifier())) {
                return true;
            }
        }
        return false;
    }
}
