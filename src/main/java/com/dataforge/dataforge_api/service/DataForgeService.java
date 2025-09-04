package com.dataforge.dataforge_api.service;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DataForgeService {

    private final Faker faker = new Faker(new Locale("pt-BR")); // Locale pt-BR para dados realistas
    private final Random random = new Random();

    public List<Map<String, Object>> generateData(Map<String, String> schema, int count) {
        final List<Map<String, Object>> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final Map<String, Object> item = new HashMap<>();
            for (Map.Entry<String, String> entry : schema.entrySet()) {
                final String key = entry.getKey();
                final String type = entry.getValue();
                final Object value = generateField(type);
                if (value != null) {
                    item.put(key, value);
                }
            }
            result.add(item);
        }
        return result;
    }

    private Object generateField(final String type) {
        final String[] typeParts = type.split(":", 2);
        final String baseType = typeParts[0].toLowerCase(Locale.ROOT).trim();
        final String param = typeParts.length > 1 ? typeParts[1].trim() : null;

        switch (baseType) {
            // 📌 Dados pessoais
            case "nome_completo":
                return faker.name().fullName();
            case "nome":
                return faker.name().firstName();
            case "sobrenome":
                return faker.name().lastName();
            case "email":
                return faker.internet().emailAddress();
            case "telefone":
                return faker.phoneNumber().cellPhone();
            case "cpf":
                return generateCPF();
            case "cnpj":
                return generateCNPJ();

            // 📌 Endereço
            case "endereco":
                return faker.address().streetAddress();
            case "cidade":
                return faker.address().city();
            case "estado":
                return faker.address().state();
            case "cep":
                return faker.address().zipCode();
            case "latitude":
                return faker.address().latitude();
            case "longitude":
                return faker.address().longitude();

            // 📌 Valores genéricos
            case "uuid":
                return UUID.randomUUID().toString();
            case "boolean":
            case "booleano":
                return faker.bool().bool();
            case "valor_monetario":
                return String.format("R$ %.2f", faker.number().randomDouble(2, 1, 10000));

            case "numero":
                if (param != null) {
                    final String[] limits = param.split("-");
                    final int min = Integer.parseInt(limits[0]);
                    final int max = Integer.parseInt(limits[1]);
                    return faker.number().numberBetween(min, max);
                }
                return faker.number().randomDigit();

            case "inteiro":
                if (param != null) {
                    final String[] limits = param.split("-");
                    final int min = Integer.parseInt(limits[0]);
                    final int max = Integer.parseInt(limits[1]);
                    return faker.number().numberBetween(min, max);
                }
                return faker.number().randomNumber();

            // 📌 Datas e tempo
            case "data_passada":
                return faker.date().past(365, TimeUnit.DAYS);
            case "data_futura":
                return faker.date().future(365, TimeUnit.DAYS);
            case "timestamp":
                return Instant.now().getEpochSecond();
            case "data_nascimento":
                return faker.date().birthday();

            // 📌 Internet
            case "url":
                return faker.internet().url();
            case "dominio":
                return faker.internet().domainName();
            case "ip":
                return faker.internet().publicIpV4Address();
            case "http_status":
                return faker.number().numberBetween(100, 599);

            // 📌 Conteúdo
            case "frase":
                return faker.lorem().sentence();
            case "descricao":
            case "descrição":
                return faker.lorem().paragraph();
            case "paragrafo":
            case "parágrafo":
                return faker.lorem().paragraph();
            case "texto":
                if (param != null) {
                    final String[] lengths = param.split("-");
                    final int minLength = Integer.parseInt(lengths[0]);
                    final int maxLength = Integer.parseInt(lengths[1]);
                    return faker.lorem().characters(minLength, maxLength);
                }
                return faker.lorem().sentence();
            case "produto":
                return faker.commerce().productName();
            case "cor":
                return faker.color().name();

            // 📌 Tipos especiais
            case "enum":
                if (param != null) {
                    final String[] options = param.split(",");
                    return options[random.nextInt(options.length)].trim();
                }
                return null;

            default:
                return "Tipo desconhecido: " + baseType;
        }
    }

    // 🔢 Funções auxiliares
    private int[] randomDigits(final int count) {
        final int[] digits = new int[count];
        for (int i = 0; i < count; i++) {
            digits[i] = random.nextInt(10);
        }
        return digits;
    }

    private String generateCPF() {
        final int[] n = randomDigits(9);

        int d1 = 11 - ((n[0] * 10 + n[1] * 9 + n[2] * 8 + n[3] * 7 + n[4] * 6 + n[5] * 5 + n[6] * 4 + n[7] * 3 + n[8] * 2) % 11);
        if (d1 >= 10) d1 = 0;
        int d2 = 11 - ((n[0] * 11 + n[1] * 10 + n[2] * 9 + n[3] * 8 + n[4] * 7 + n[5] * 6 + n[6] * 5 + n[7] * 4 + n[8] * 3 + d1 * 2) % 11);
        if (d2 >= 10) d2 = 0;

        return String.format("%d%d%d.%d%d%d.%d%d%d-%d%d",
                n[0], n[1], n[2], n[3], n[4], n[5], n[6], n[7], n[8], d1, d2);
    }

    private String generateCNPJ() {
        final int[] n = randomDigits(8);
        final int n9 = 0, n10 = 0, n11 = 0, n12 = 1;

        int d1 = 11 - ((n[0] * 5 + n[1] * 4 + n[2] * 3 + n[3] * 2 + n[4] * 9 + n[5] * 8 + n[6] * 7 + n[7] * 6 +
                n9 * 5 + n10 * 4 + n11 * 3 + n12 * 2) % 11);
        if (d1 >= 10) d1 = 0;

        int d2 = 11 - ((n[0] * 6 + n[1] * 5 + n[2] * 4 + n[3] * 3 + n[4] * 2 + n[5] * 9 + n[6] * 8 + n[7] * 7 +
                n9 * 6 + n10 * 5 + n11 * 4 + n12 * 3 + d1 * 2) % 11);
        if (d2 >= 10) d2 = 0;

        return String.format("%d%d.%d%d%d.%d%d%d/%d%d%d%d-%d%d",
                n[0], n[1], n[2], n[3], n[4], n[5], n[6], n[7], n9, n10, n11, n12, d1, d2);
    }
}
