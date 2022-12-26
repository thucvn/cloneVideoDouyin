package com.uu;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SavedData {
    String link, fileName;

    @Override
    public String toString() {
        return link + (fileName == null ? "" : ("," + fileName));
    }
}
