//package com.example.strawberry.application.service.Impl;
//
//import com.example.strawberry.application.constants.MessageConstant;
//import com.example.strawberry.application.dai.IMessageRepository;
//import com.example.strawberry.application.service.IMessageService;
//import com.example.strawberry.config.exception.NotFoundException;
//import com.example.strawberry.domain.dto.MessageDTO;
//import com.example.strawberry.domain.entity.Message;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class MessageServiceImpl implements IMessageService {
//
//    private final IMessageRepository messageRepository;
//    private final ModelMapper modelMapper;
//
//    public MessageServiceImpl(IMessageRepository messageRepository, ModelMapper modelMapper) {
//        this.messageRepository = messageRepository;
//        this.modelMapper = modelMapper;
//    }
//
//    @Override
//    public Message sendMessage(MessageDTO messageDTO) {
//        Message message = modelMapper.map(messageDTO, Message.class);
//        messageRepository.save(message);
//        return message;
//    }
//
//    @Override
//    public Message deleteMessage(Long idMessage) {
//        Optional<Message> message = messageRepository.findById(idMessage);
//        checkMessageExists(message);
//        messageRepository.delete(message.get());
//        return message.get();
//    }
//
//    public void checkMessageExists(Optional<Message> message) {
//        if (message.isEmpty()) {
//            throw new NotFoundException(MessageConstant.MESSAGE_NOT_EXISTS);
//        }
//    }
//}
