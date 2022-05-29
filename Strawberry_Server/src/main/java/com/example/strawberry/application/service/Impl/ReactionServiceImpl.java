package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.dai.IReactionRepository;
import com.example.strawberry.application.service.IReactionService;
import com.example.strawberry.application.utils.UploadFile;
import com.example.strawberry.domain.dto.ReactionDTO;
import com.example.strawberry.domain.entity.Reaction;
import com.example.strawberry.domain.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ReactionServiceImpl implements IReactionService {

    private final UploadFile uploadFile;
    private final IReactionRepository reactionRepository;
    private final ModelMapper modelMapper;

    public ReactionServiceImpl(UploadFile uploadFile, IReactionRepository reactionRepository, ModelMapper modelMapper) {
        this.uploadFile = uploadFile;
        this.reactionRepository = reactionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Reaction createReaction(ReactionDTO reactionDTO, MultipartFile fileImage) {
        Reaction reaction = modelMapper.map(reactionDTO, Reaction.class);
        reaction.setLinkReaction(uploadFile.getUrlFromFile(fileImage));
        return reactionRepository.save(reaction);
    }
}
