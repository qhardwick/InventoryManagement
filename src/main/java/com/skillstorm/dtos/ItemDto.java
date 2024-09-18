package com.skillstorm.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skillstorm.entities.Item;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {

    @Min(value = 0, message = "{item.id.value}")
    private int id;

    @Size(min = 3, max = 20, message = "{item.name.size}")
    private String name;

    @Min(value = 1, message = "{item.volume.min}")
    @Max(value = 500, message = "{item.volume.max")
    private int volume;

    public ItemDto(Item item) {
        id = item.getId();
        name = item.getName();
        volume = item.getVolume();
    }

    @JsonIgnore
    public Item getItem() {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setVolume(volume);

        return item;
    }
}
