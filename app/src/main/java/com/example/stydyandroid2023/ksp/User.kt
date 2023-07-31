package com.example.stydyandroid2023.ksp

import com.github.yanneckreiss.kconmapper.annotations.KConMapper
import com.github.yanneckreiss.kconmapper.annotations.KConMapperProperty
import java.util.UUID

class UserDTO(uid: UUID, name: String, age: Int) {

}

@KConMapper(
    toClasses = [UserDTO::class, UserEntity::class],
    fromClasses = [UserDTO::class, UserEntity::class]
)
data class User(
    val uid: UUID,
    val name: String,
    @KConMapperProperty(aliases = ["age"])
    val age: Int
) {
    // SE AS PROPRIEDDADES NAO BATAREM NA NOMEMCLATURA
    //QUE VEM DO BACKEND É DIFERENTE DA SUA ENTIDADE DE DOMINIO
    //FACA ASSIM, NOS PARAMETROS DO CONSTUTOR
    // @KConMapperProperty(aliases = ["ageInYears"])
    //    val age: Int

    /* fun toUserDTO() = UserDTO(
         uid = uid,
         name = name,
         age = age
     )

     fun toUserEntity() = UserEntity(
         uid = uid,
         name = name,
         age = age
     )*/
}

class UserEntity(uid: UUID, name: String, age: Int) {

}
/*
KConMapper vai gerar os mappers:
   fromClasses = [UserDTO::class, UserEntity::class]
        UserDTO.toUser() = User ....
        UserEntity.toUser() =User ...
   toClasses = [UserDTO::class, UserEntity::class],
        User.toUserEntity() =UserDTO...
        User.toUSerDTO() =USerEntity...
 */