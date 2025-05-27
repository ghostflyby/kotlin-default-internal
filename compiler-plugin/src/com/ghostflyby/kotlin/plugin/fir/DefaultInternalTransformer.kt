package com.ghostflyby.kotlin.plugin.fir

import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.hasModifier
import org.jetbrains.kotlin.fir.copyWithNewDefaults
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.utils.visibility
import org.jetbrains.kotlin.fir.extensions.FirStatusTransformerExtension
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.lexer.KtTokens

class DefaultInternalTransformer(session: FirSession) : FirStatusTransformerExtension(session) {
  override fun needTransformStatus(declaration: FirDeclaration): Boolean =
    declaration is FirMemberDeclaration &&
        declaration.run {
          origin == FirDeclarationOrigin.Source &&
              !symbol.hasModifier(KtTokens.PUBLIC_KEYWORD)
              && (visibility == Visibilities.Unknown || visibility == Visibilities.Public)
        }

  override fun transformStatus(status: FirDeclarationStatus, declaration: FirDeclaration): FirDeclarationStatus {
    return status.copyWithNewDefaults(
      visibility = Visibilities.Internal
    )
  }

  override fun transformStatus(
    status: FirDeclarationStatus,
    propertyAccessor: FirPropertyAccessor,
    containingClass: FirClassLikeSymbol<*>?,
    containingProperty: FirProperty?,
    isLocal: Boolean
  ): FirDeclarationStatus {
    return if (containingProperty != null)
      status.copyWithNewDefaults(
        visibility = containingProperty.visibility
      )
    else
      status
  }

  override fun transformStatus(
    status: FirDeclarationStatus,
    constructor: FirConstructor,
    containingClass: FirClassLikeSymbol<*>?,
    isLocal: Boolean
  ): FirDeclarationStatus {
    return if (constructor.isPrimary && containingClass != null)
      status.copyWithNewDefaults(containingClass.visibility)
    else
      transformStatus(status, constructor)
  }

}
