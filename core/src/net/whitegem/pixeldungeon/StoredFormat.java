package net.whitegem.pixeldungeon;

import java.util.ArrayList;

/**
 * Created by Carl-Station on 01/23/15.
 */
public class StoredFormat
{
    private ArrayList<String> keys;
    private ArrayList<String> values;

    public StoredFormat()
    {
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    public String get(String key)
    {
        if (contains(key))
        {
            for (int i = keys.size() - 1; i >= 0; i--)
            {
                if (keys.get(i).equals(key))
                {
                    return values.get(i);
                }
            }
        }
        return "";
    }

    public void add(String key, String value)
    {
        keys.add(key);
        values.add(value);
        remove();
    }

    public void put(String key, String value)
    {
        add(key, value);
    }

    public boolean contains(String key)
    {
        return keys.contains(key);
    }

    private void remove()
    {
        if (keys.size() > 20)
        {
            keys.remove(0);
            values.remove(0);
        }
    }
}
