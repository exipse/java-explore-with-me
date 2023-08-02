package ru.practicum.explore_with_me.requests.dto;

//Статус заявки на участие в событии
public enum Status {  //в ожидании
    PENDING,
    //подтверждена
    CONFIRMED,
    //отклонена
    REJECTED,
    //отменено
    CANCELED
}
