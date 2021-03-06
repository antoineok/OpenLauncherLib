package fr.theshark34.openlauncherlib.configuration.api.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by NeutronStars on 14/07/2017
 */
public final class JSONReader
{
    private final Logger logger;
    private final String json;

    public JSONReader(Logger logger, String path) throws IOException
    {
        this(logger, new File(path));
    }

    public JSONReader(Logger logger, File file) throws IOException
    {
        this(logger, new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
    }

    public JSONReader(Logger logger, Reader reader) throws IOException
    {
        this(logger, new BufferedReader(reader));
    }

    public JSONReader(Logger logger, BufferedReader reader) throws IOException
    {
        this.logger = logger;
        json        = load(reader);
    }

    private String load(BufferedReader reader) throws IOException
    {
        StringBuilder builder = new StringBuilder();

        while (reader.ready()) builder.append(reader.readLine());

        reader.close();

        return builder.length() == 0 ? "[]" : builder.toString();
    }

    public static <E> List<E> toList(Logger logger, String path)
    {
        return toList(logger, new File(path));
    }

    public static <E> List<E> toList(Logger logger, File file)
    {
        if (!file.exists()) return new ArrayList<>();
        try
        {
            return toList(logger, new InputStreamReader(new FileInputStream(file)));
        } catch (IOException e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public static <E> List<E> toList(Logger logger, Reader reader)
    {
        return toList(logger, new BufferedReader(reader));
    }

    @SuppressWarnings("unchecked")
	public static <E> List<E> toList(Logger logger, BufferedReader bufferedReader)
    {
        List<E> list = new ArrayList<>();

        try
        {
            JSONReader reader = new JSONReader(logger, bufferedReader);
            JSONArray  array  = reader.toJSONArray();
            for (int i = 0; i < array.length(); i++)
            {
                try
                {
                    list.add((E)array.get(i));
                } catch (ClassCastException ignored) {}
            }
        } catch (IOException e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return list;
    }

    public static <V> Map<String, V> toMap(Logger logger, String path)
    {
        return toMap(logger, new File(path));
    }

    public static <V> Map<String, V> toMap(Logger logger, File file)
    {
        if (!file.exists()) return new HashMap<>();
        try
        {
            return toMap(logger, new InputStreamReader(new FileInputStream(file)));
        } catch (IOException e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return new HashMap<>();
    }

    public static <V> Map<String, V> toMap(Logger logger, Reader reader)
    {
        return toMap(logger, new BufferedReader(reader));
    }

    @SuppressWarnings("unchecked")
	public static <V> Map<String, V> toMap(Logger logger, BufferedReader bufferedReader)
    {
        Map<String, V> map = new HashMap<>();

        try
        {
            JSONReader reader = new JSONReader(logger, bufferedReader);
            JSONObject object = reader.toJSONObject();
            for (String key : object.keySet())
            {
                try
                {
                    map.put(key, (V)object.get(key));
                } catch (ClassCastException ignored) {}
            }
        } catch (IOException e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return map;
    }

    public JSONArray toJSONArray()
    {
        return new JSONArray(json);
    }

    public JSONObject toJSONObject()
    {
        return new JSONObject(json);
    }

    public Logger getLogger()
    {
        return logger;
    }
}
