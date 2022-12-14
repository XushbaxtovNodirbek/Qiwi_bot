package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {
    private final String QIWI_NUMBER = "998935777176";
    private final String QIWI_TOKEN = "7eebfa0aba18595ded72ae94556c5716";
    private Step step = new Step();
    private HashMap<Long, BotUser> userMap = new HashMap<>();
    private final String TOKEN = "5722693822:AAG2yrrna3--TrLw2hrCcQH6NgNI4KKZf4M";
    private final String BOT_USER = "qiwi_test1_bot";

    @Override
    public String getBotUsername() {
        return BOT_USER;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            System.out.println(update);
            String text;
            Message message = update.getMessage();
            long chat_id = message.getChatId();
            if (message.getText().equals("/start")) {
                if (userMap.get(chat_id) == null) {
                    BotUser user = new BotUser();
                    userMap.put(chat_id, user);
                    text = "Assalomu alaykum botimizga xush kelibsiz!\nBotdan foydalanish uchun kabinetingizga o'z hisob raqamingizni kiriting";
                    Send(text, chat_id, MainBttons());
                    user.setStep(step.getMAIN());
                } else {
                    Send("Assalomu alaykum!", chat_id);
                    userMap.get(chat_id).setStep(step.getMAIN());
                }
            } else if (userMap.get(chat_id).getStep().equals(step.getMAIN())) {
                if (message.getText().equals("Ayriboshlash")) userMap.get(chat_id).setStep(step.getCONVERT());
                else if (message.getText().equals("\uD83D\uDCC9Kurs")) {
                    text = """
                            buy:
                            1 rub=170 uzs
                            sell:
                            1 rub=175 uzs                       
                            """;
                    Send(text, chat_id);
                } else if (message.getText().equals("\uD83D\uDDC4kabinetim")) {
                    userMap.get(chat_id).setStep(step.getUSER_INFO());
                    text="Kabinet:\n" +
                            "Qiwi : "+userMap.get(chat_id).getQiwi_number()+"\n" +
                            "Humo : "+userMap.get(chat_id).getHumo_number()+"\n" +
                            "Uzcard : "+userMap.get(chat_id).getUzcard_number();
                    Send(text, chat_id, UserBttons());
                } else if (message.getText().equals("\uD83D\uDCF2aloqa")) Send("@xushbaxtov", chat_id);
            } else if (userMap.get(chat_id).getStep().equals(step.getUSER_INFO())) {
                if (message.getText().equals("Qiwi hamyon kiritish")){
                    userMap.get(chat_id).setStep(step.getREGISTER_QIWI());
                    text="Qiwi hamyoningiz raqamini yuboring (+ belgisi bilan):";
                    Send(text,chat_id,BackButton());
                }else if (message.getText().equals("Humo kard kiritish")){
                    userMap.get(chat_id).setStep(step.getREGISTER_HUMO());
                    text="Karta raqamini ortiqcha belgilarsiz kiriting";
                    Send(text,chat_id,BackButton());
                }else if (message.getText().equals("Uzcard kiritish")){
                    userMap.get(chat_id).setStep(step.getREGISTER_UZCARD());
                    text="Karta raqamini ortiqcha belgilarsiz kiriting";
                    Send(text,chat_id,BackButton());
                }else if (message.getText().equals("Menyuga qaytish")){
                    userMap.get(chat_id).setStep(step.getMAIN());
                    text="Menyu:";
                    Send(text,chat_id,MainBttons());
                }
            } else if (userMap.get(chat_id).getStep().equals(step.getREGISTER_QIWI())) {
                userMap.get(chat_id).setQiwi_number(message.getText());
                userMap.get(chat_id).setStep(step.getUSER_INFO());
                text="Kabinet:\n" +
                        "Qiwi : "+userMap.get(chat_id).getQiwi_number()+"\n" +
                        "Humo : "+userMap.get(chat_id).getHumo_number()+"\n" +
                        "Uzcard : "+userMap.get(chat_id).getUzcard_number();
                Send(text,chat_id,UserBttons());
            }else if (userMap.get(chat_id).getStep().equals(step.getREGISTER_HUMO())) {
                userMap.get(chat_id).setHumo_number(message.getText());
                userMap.get(chat_id).setStep(step.getUSER_INFO());
                text="Kabinet:\n" +
                        "Qiwi : "+userMap.get(chat_id).getQiwi_number()+"\n" +
                        "Humo : "+userMap.get(chat_id).getHumo_number()+"\n" +
                        "Uzcard : "+userMap.get(chat_id).getUzcard_number();
                Send(text,chat_id,UserBttons());
            }else if (userMap.get(chat_id).getStep().equals(step.getREGISTER_UZCARD())) {
                userMap.get(chat_id).setUzcard_number(message.getText());
                userMap.get(chat_id).setStep(step.getUSER_INFO());
                text="Kabinet:\n" +
                        "Qiwi : "+userMap.get(chat_id).getQiwi_number()+"\n" +
                        "Humo : "+userMap.get(chat_id).getHumo_number()+"\n" +
                        "Uzcard : "+userMap.get(chat_id).getUzcard_number();
                Send(text,chat_id,UserBttons());
            }

        }
    }

    private void Send(String text, long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void Send(String text, long chatId, ReplyKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(markup);


        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup MainBttons() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button;
        button = new KeyboardButton("\uD83D\uDCB1Ayriboshlash");
        row.add(button);
        button = new KeyboardButton("\uD83D\uDCC9Kurs");
        row.add(button);
        rows.add(row);
        row = new KeyboardRow();
        button = new KeyboardButton("\uD83D\uDDC4kabinetim");
        row.add(button);
        button = new KeyboardButton("\uD83D\uDCF2aloqa");
        row.add(button);
        rows.add(row);
        markup.setKeyboard(rows);
        return markup;
    }

    private ReplyKeyboardMarkup UserBttons() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button;
        button = new KeyboardButton("Qiwi hamyon kiritish");
        row.add(button);
        button = new KeyboardButton("Humo kard kiritish");
        row.add(button);
        rows.add(row);
        row = new KeyboardRow();
        button = new KeyboardButton("Uzcard kiritish");
        row.add(button);
        button = new KeyboardButton("Menyuga qaytish");
        row.add(button);
        rows.add(row);
        markup.setKeyboard(rows);
        return markup;
    }
    private ReplyKeyboardMarkup BackButton(){
        ReplyKeyboardMarkup markup=new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        List<KeyboardRow> rows=new ArrayList<>();
        KeyboardRow row=new KeyboardRow();
        KeyboardButton button=new KeyboardButton("Ortga");
        row.add(button);
        rows.add(row);
        markup.setKeyboard(rows);
        return markup;
    }


}
