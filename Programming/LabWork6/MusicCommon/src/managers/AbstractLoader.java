package managers;

import java.util.List;

public abstract class AbstractLoader {
     public <T extends LoadDescription<?>> T enter(String message, T description, BaseTextReceiver textReceiver) {
        if (isWrapper(description.getType())) {
            return (T) enterWrapper(message, (LoadDescription<Number>) description, textReceiver);
        } else if (description.getType().equals(String.class)) {
            return (T) enterString(message, (LoadDescription<String>) description, textReceiver);
        } else if (description.getType().isEnum()){
            return (T) enterEnum(message, (LoadDescription<Enum>) description, textReceiver);
        } else {
            return enterComposite(message, description, textReceiver);
        }
    }

    public abstract <T extends LoadDescription<Enum>> T enterEnum(String message, T description, BaseTextReceiver textReceiver);

    public abstract <T extends LoadDescription<Number>> T enterWrapper(String message, T description, BaseTextReceiver textReceiver);

    public abstract LoadDescription<String> enterString(String message, LoadDescription<String> description, BaseTextReceiver textReceiver);

    private <T extends LoadDescription<?>> T enterComposite(String message, T description, BaseTextReceiver textReceiver) {
        textReceiver.print(message);
        List<LoadDescription<?>> fields = description.getFields();
        for (var field : description.getFields()) {
            fields.add(enter(field.getDescription(), field, textReceiver));
        }
        description.getFields().clear();
        description.setFieldsOfObject(fields);
        return description;
    }

    private boolean isWrapper(Class<?> type) {
        return type.equals(Integer.class) || type.equals(Long.class) || type.equals(Double.class)
                || type.equals(Float.class) || type.equals(Short.class) || type.equals(Byte.class)
                || type.equals(Character.class) || type.equals(Boolean.class);
    }
}
