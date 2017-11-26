package com.pi.common.utils.mapper.bean;

import com.pi.common.utils.enums.EnumEntity;
import com.pi.common.utils.enums.EnumEntityFactory;
import com.pi.common.utils.enums.EnumEntityMapping;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;

public class EnumEntityMappingConverter extends CustomConverter<Object, Object> {

    @Override
    public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
        return (EnumEntity.class.isAssignableFrom(sourceType.getRawType())
                && EnumEntityMapping.class.isAssignableFrom(destinationType.getRawType()))
                || (EnumEntityMapping.class.isAssignableFrom(sourceType.getRawType())
                        && EnumEntity.class.isAssignableFrom(destinationType.getRawType()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object convert(Object source, Type<? extends Object> destinationType) {

        // if same class then return source
        if (source != null && source.getClass().equals(destinationType.getRawType())) {
            return source;
        }

        if (EnumEntityMapping.class.isAssignableFrom(destinationType.getRawType())) {
            return EnumEntityFactory.getUnchecked((Class<EnumEntity<?>>) destinationType.getRawType(), source);
        } else {
            return ((EnumEntityMapping<?, EnumEntity<?>>) source).getCorrespondingEnumEntity();
        }
    }

}
